# Geometric Modelling Labs (Michaelmas 2024)

This repository contains completed implementations for:

- `Lab 1`: 2D convex hull (incremental algorithm in Java).
- `Lab 2`: constructive solid geometry mug model (SvLis C++).
- `Lab 3`: Bezier curve degree elevation (Java).

Reference documentation used:
- Practical PDFs in each lab directory.
- SvLis online documentation: <https://adrianbowyer.com/Publications/SvLiS-book/node1.html>

## Build And Run

### Lab 1 (Java Convex Hull GUI)
```bash
cd "Lab 1"
javac *.java
java ConvexHull
```

CLI test harness:
```bash
java HullCliTest sample.data
```

### Lab 2 (SvLis CSG Mug)
```bash
cd "Lab 2"
make CSGmug
./CSGmug
```

The program writes `mug.mod`.  
If `plot=true` in `CSGmug.cxx`, an interactive view opens. If `raytrace=true`, it writes `raytraced.ppm`.

### Lab 3 (Java Bezier Degree Elevation GUI)
```bash
cd "Lab 3"
javac *.java
java Bezier
```

CLI test harness:
```bash
java BezierCliTest sample.data 12
```

## Testing Evidence

Each lab directory contains `TEST_COMMANDS.txt` with exact reproducible commands and expected outcomes.

## Public Interface Changes

No public interface changes.
