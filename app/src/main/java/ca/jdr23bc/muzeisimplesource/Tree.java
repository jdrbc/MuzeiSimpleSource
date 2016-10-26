package ca.jdr23bc.muzeisimplesource;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

// TODO-jdr horribly inefficient probably
// http://algorithmicbotany.org/papers/colonization.egwnp2007.html
public class Tree {

    public float minDist = 5;
    public float maxDist = 500;

    public int maxHeight;
    public int maxWidth;
    public int rootHeight;

    public Random rand = new Random();
    public ArrayList<Leaf> leaves = new ArrayList<>();
    public ArrayList<Branch> branches = new ArrayList<>();
    public ArrayList<Branch> endBranches = new ArrayList<>();
    public int minBranchWidth = 5;

    public int leafCount = 100;
    // Width to length ratio
    public float leafWidthRatio = 1;
    public float leafLength = 60f;
    public float branchWidthStep = 1f;
    public int maxBranchWidth = 200;
    public int branchLength = 10;

    public static Double getDist(PointF p1, PointF p2) {
        return Math.hypot(p1.x - p2.x, p1.y - p2.y);
    }

    public static PointF add(PointF p1, PointF p2) {
        return new PointF(p1.x + p2.x, p1.y + p2.y);
    }

    public static PointF sub(PointF p1, PointF p2) {
        return new PointF(p1.x - p2.x, p1.y - p2.y);
    }

    public static PointF mult(PointF p1, int length) {
        return new PointF(p1.x * length, p1.y * length);
    }

    public static PointF mult(PointF p1, double length) {
        return new PointF(new Float(p1.x * length), new Float(p1.y * length));
    }

    public static PointF normalize(PointF p) {
        Double len = getDist(p, new PointF(0, 0));
        return new PointF(new Float(p.x / len), new Float(p.y / len));
    }

    public Tree(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        int minRootHeight = maxHeight / 8;
        this.rootHeight = rand.nextInt(maxHeight / 4) + minRootHeight;

        // Create tree
        // Populate leaves
        for (int i = 0; i < leafCount; i++) {
            leaves.add(new Leaf(this));
        }

        // Root starts in middle bottom and grows straight up
        Branch root = new Branch(new PointF(maxWidth / 2, maxHeight), new PointF(0, -1), this);
        branches.add(root);

        // Need to ensure that the root is within range of a leaf
        boolean withinRange = false;
        Branch currBranch = root;
        while(!withinRange) {
            for (Leaf leaf : leaves) {
                Double dist = getDist(leaf.pos, currBranch.pos);
                if (dist < maxDist) {
                    withinRange = true;
                }
            }

            // If not within range of a leaf
            if (!withinRange) {
                // Grow the root to get closer to a leaf
                Log.d(SimpleArtSource.TAG, "growing root");
                currBranch = currBranch.next();
                branches.add(currBranch);
            }
        }
    }

    public void grow() {
        ArrayList<Leaf> freeLeaves = new ArrayList<>();
        freeLeaves.addAll(leaves);

        int lastBranchesToGrowCount = 0;
        while(!freeLeaves.isEmpty()) {
            ArrayList<Branch> branchesToGrow = new ArrayList<>();
            ArrayList<Leaf> connectedLeaves = new ArrayList<>();
            for (Leaf leaf : freeLeaves) {
                Branch closestBranch = null;
                double closestDist = 0;
                // Find the closest branch
                for (Branch branch : branches) {
                    double dist = getDist(leaf.pos, branch.pos);

                    if (dist < minDist) {
                        // Leaf has been reached
                        connectedLeaves.add(leaf);
                        closestBranch = null;
                        break;
                    } else if (dist > maxDist) {
                        // Leaf is too far away
                        continue;
                    } else if (closestBranch == null || dist < closestDist) {
                        // Found a new closest branch
                        closestBranch = branch;
                        closestDist = dist;
                    }
                }

                // Point the branch towards the leaf
                if (closestBranch != null) {
                    closestBranch.attractors.add(leaf.pos);
                    if (!branchesToGrow.contains(closestBranch)) {
                        branchesToGrow.add(closestBranch);
                    }
                }
            }
            Log.d(SimpleArtSource.TAG, "connectedLeaves: " + connectedLeaves.size());
            Log.d(SimpleArtSource.TAG, "freeLeaves: " + freeLeaves.size());
            Log.d(SimpleArtSource.TAG, "branchesToGrow: " + branchesToGrow.size());
            Log.d(SimpleArtSource.TAG, "minDist: " + minDist);
            Log.d(SimpleArtSource.TAG, "maxDist: " + maxDist);

            // Prune any connected leaves
            freeLeaves.removeAll(connectedLeaves);

            // Grow any attracted branches
            for (Branch branch : branchesToGrow) {
                endBranches.remove(branch);
                Branch next = branch.next();
                endBranches.add(next);
                branches.add(next);
            }

            // Sometimes a branch will get stuck between a few leaves...
            if (lastBranchesToGrowCount == branchesToGrow.size()) {
                this.minDist += 0.05;
                this.maxDist += 0.1;
            }
            lastBranchesToGrowCount = branchesToGrow.size();
        }

        // Set branch widths
        for (Branch branch : endBranches) {
            branch.setWidth(minBranchWidth);
        }
    }

    public void drawBranches(Canvas c, Paint p) {
        p.setStrokeCap(Paint.Cap.ROUND);
        for (Branch branch : branches) {
            branch.draw(c, p);
        }
    }

    public void drawLeaves(Canvas c, Paint p) {
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        p.setAlpha(200);
        for (Leaf leaf : leaves) {
            leaf.draw(c, p);
        }
    }

    public class Branch {
        float width = 0;
        PointF pos;
        PointF dir;
        Branch parent;
        Tree tree;

        // List of attracting points
        ArrayList<PointF> attractors = new ArrayList<>();

        public Branch(PointF pos, PointF dir, Tree tree) {
            this.pos = pos;
            this.dir = dir;
            this.tree = tree;
        }

        public Branch(PointF pos, PointF dir, Branch parent) {
            this.tree = parent.tree;
            this.pos = pos;
            this.dir = dir;
            this.parent = parent;
        }

        public Branch next() {
            PointF attractedDir = new PointF(dir.x, dir.y);

            Double distSums = 0.0;
            for (PointF attractor : attractors) {
                distSums += getDist(attractor, pos);
            }

            for (PointF attractor : attractors) {
                double dist = getDist(attractor, pos);
                PointF dirToAttractor = sub(attractor, pos);
                dirToAttractor = normalize(dirToAttractor);
                dirToAttractor = mult(dirToAttractor, dist / distSums);
                attractedDir = add(attractedDir, dirToAttractor);
            }

            attractedDir = normalize(attractedDir);
            attractors.clear();
            return new Branch(add(pos, mult(attractedDir, tree.branchLength)), attractedDir, this);
        }


        public void setWidth(float width) {
            this.width = Math.min(width, tree.maxBranchWidth);
            if (this.parent != null && this.parent.width < width) {
                this.parent.setWidth(width + tree.branchWidthStep);
            }
        }

        public void draw(Canvas canvas, Paint paint) {
            // Draw line
            if (parent != null) {
                paint.setStrokeWidth(width);
                canvas.drawLine(pos.x, pos.y, parent.pos.x, parent.pos.y, paint);
            }
        }
    }

    public class Leaf {
        PointF pos;
        Tree tree;

        public Leaf (Tree t) {
            this.tree = t;
            this.pos = new PointF(t.rand.nextInt(t.maxWidth), t.rand.nextInt(t.maxHeight - t.rootHeight));
        }

        public void draw(Canvas canvas, Paint paint) {
            Path leaf = new Path();
            leaf.moveTo(pos.x, pos.y);

            // Get vector from middle top of screen to the leaf to get tip of leaf
            PointF top = new PointF(rand.nextInt(canvas.getWidth() / 2) + canvas.getWidth() / 4, -canvas.getHeight()/4);
            PointF topToPos = normalize(sub(pos, top));
            PointF tip = add(pos, mult(topToPos, tree.leafLength));
            PointF midTip = add(pos, mult(topToPos, tree.leafLength / 2));

            // Find sides
            float width =  tree.leafLength * tree.leafWidthRatio;
            PointF side1 = add(midTip, mult(new PointF(-topToPos.y, topToPos.x), width/2));
            PointF side2 = add(midTip, mult(new PointF(topToPos.y, -topToPos.x), width/2));

//            leaf.lineTo(side1.x, side1.y);
//            leaf.lineTo(tip.x, tip.y);
//            leaf.lineTo(side2.x, side2.y);
//            leaf.lineTo(pos.x, pos.y);
            leaf.quadTo(side1.x, side1.y, tip.x, tip.y);
            leaf.quadTo(side2.x, side2.y, pos.x, pos.y);
            leaf.close();

            // draw circle
            canvas.drawPath(leaf, paint);
        }
    }
}
