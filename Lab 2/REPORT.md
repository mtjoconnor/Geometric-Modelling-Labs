# Lab 2 Report: Constructive Solid Geometry Mug

## Checklist Coverage

- Updated `CSGmug.cxx` to construct a mug model using CSG primitives and Boolean operators.
- Kept practical workflow compatibility (`make CSGmug`, output `mug.mod`, existing plot/raytrace toggles).
- Main required task (make a mug) implemented.

## Implementation Notes

- File changed: `CSGmug.cxx`.
- Mug geometry:
  - **Body shell**: outer cylinder minus inner cylinder, both clipped by cuboids for wall/base thickness.
  - **Lip ring**: a white ring near the rim for a ceramic edge.
  - **Decorative band**: thin gold ring below the lip.
  - **Handle**: side-oriented torus loop (vertical mug-handle orientation), clipped and blended with small joint spheres.
  - **Final model**: union of body, lip, band, and handle.
- Rendering quality:
  - `set_user_grad_fac(0.20)` is used before faceting to make curved surfaces smoother.
- Colors are used to match the target style:
  - red body/handle, white lip, gold band.

## Written Answers (Boolean Operator Semantics)

Given sets `A` and `B`:
- `A | B`: union (all points in either set),
- `A & B`: intersection (points common to both),
- `A - B`: subtraction (points in `A` not in `B`),
- `A ^ B`: symmetric difference (points in exactly one of the sets).

## Optional Task Status

- Optional creative second model: not implemented in this submission.

## Validation Summary

- `make CSGmug`: succeeds when SvLis headers/libs are available either:
  - in `Lab 2/SvLis`, or
  - in an external path provided via `SVLIS_DIR=/path/to/SvLis`.
- On macOS the `Makefile` links with:
  - `-framework GLUT -framework OpenGL`
- Build output produced:
  - `./CSGmug`
- Runtime behavior:
  - Running `./CSGmug` should generate `mug.mod` and open the interactive plot window when `plot=true`.

## Notes For Reproducibility

1. Make sure SvLis is available:
   - local checkout at `Lab 2/SvLis`, or
   - external checkout path passed as `SVLIS_DIR=...`.
2. Build from `Lab 2`:
   - `make CSGmug`
3. Run:
   - `./CSGmug`
- Expected artifacts:
  - `libsvlis.a` (inside chosen SvLis directory)
  - `CSGmug`
  - `mug.mod` (after running)
