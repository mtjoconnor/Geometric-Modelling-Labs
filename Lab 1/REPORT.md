# Lab 1 Report: Incremental 2D Convex Hull

## Checklist Coverage

- Implemented lexicographic sort by `(x, y)` in `sortByXY()`.
- Implemented convex hull construction in `hullIncremental()` using an incremental visible-chain update.
- Used integer orientation predicates (`left`, `leftOn`, `collinear`) throughout.
- Added robust handling for duplicate points and collinear/degenerate cases (optional task).
- Added CLI verification harness: `HullCliTest.java`.

## Implementation Notes

- File changed: `MyPointSet.java`.
- Hull points are stored in `theHull` and drawn by existing GUI pipeline.
- The implementation:
  - sorts points lexicographically,
  - removes exact duplicates,
  - initializes the hull from the first non-collinear triple,
  - finds upper/lower tangent boundaries for each new point,
  - removes the lit chain and inserts the new point.
- Optional robustness:
  - all-collinear input returns the two extreme endpoints,
  - duplicate points are ignored,
  - a monotonic-chain fallback is used when extreme x-columns are degenerate (e.g. `box.data` style inputs).

## Written Answers

### What happens with duplicate clicks at the same point?
Without special handling, duplicates can break assumptions about unique ordered points and may create redundant/unstable hull steps. The implementation now removes duplicates before hull construction.

### Situations where a valid hull exists but basic incremental code may fail without special cases
- fewer than 3 unique points,
- all points collinear,
- repeated points,
- heavy degeneracy at leftmost/rightmost x-columns causing ambiguous initialization.

### What should happen when all points are collinear?
The hull should collapse to the segment joining the two extreme points. The implementation returns those two endpoints.

## Validation Summary

- `javac *.java`: pass.
- `java HullCliTest sample.data`: pass (`HULL_NPOINTS=5`).
- `java HullCliTest box.data`: pass (`HULL_NPOINTS=4`, expected box corners).
- `java HullCliTest box.data.2`: pass (`HULL_NPOINTS=2`, collinear extremes).
- Negative test (`java HullCliTest`): correct usage error and non-zero exit.
