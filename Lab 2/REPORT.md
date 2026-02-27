# Lab 2 Report: Constructive Solid Geometry Mug

## Checklist Coverage

- Updated `CSGmug.cxx` to construct a mug model using CSG primitives and Boolean operators.
- Kept practical workflow compatibility (`make CSGmug`, output `mug.mod`, existing plot/raytrace toggles).
- Main required task (make a mug) implemented.

## Implementation Notes

- File changed: `CSGmug.cxx`.
- Mug geometry:
  - **Body**: finite cylindrical shell from `cylinder` intersected with a height-limiting cuboid.
  - **Cavity**: inner cylinder intersected with a slightly higher cuboid, subtracted to leave base thickness.
  - **Handle**: clipped `torus` segment overlapping the shell to avoid infinitely-thin touching.
  - **Final model**: `mug_shell | handle`.
- Colors are applied for visual separation in interactive rendering.

## Written Answers (Boolean Operator Semantics)

Given sets `A` and `B`:
- `A | B`: union (all points in either set),
- `A & B`: intersection (points common to both),
- `A - B`: subtraction (points in `A` not in `B`),
- `A ^ B`: symmetric difference (points in exactly one of the sets).

## Optional Task Status

- Optional creative second model: not implemented in this submission.

## Validation Summary

- `make CSGmug`: **fails in this environment** because SvLis headers are not installed:
  - `fatal error: 'svlis.h' file not found`
- Code remains build-compatible with the provided Makefile for a correctly configured SvLis setup (e.g. lab machines or local SvLis install).

## Notes For Reproducibility

- Practical validation requires an environment with SvLis development headers/libraries available on include/library paths used by `Makefile`.
