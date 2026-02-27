# Lab 3 Report: Bezier Degree Elevation

## Checklist Coverage

- Implemented `elevateOnce()` in `MyPolygon.java`.
- Preserved original control polygon and elevated only the curve polygon.
- Supported repeated elevation: each button press elevates from the latest elevated curve.
- Used double-precision intermediate arrays and rounded only for display storage.
- Added CLI verification harness: `BezierCliTest.java`.

## Implementation Notes

- File changed: `MyPolygon.java`.
- Behavior implemented:
  - first elevation initializes from control points (`xpoints`, `ypoints`),
  - subsequent elevations initialize from `Elevated`,
  - in-place backward update applies the degree-elevation recurrence,
  - endpoints are preserved exactly,
  - `elevated` flag is set and degree is printed each elevation.

## Written Answers

### Suitable preset number of elevations for smooth curves
A value around **10-15 elevations** gives a smooth curve in most practical cases.  
This submission verified `12` elevations using the CLI harness (`ELEVATED_NPOINTS=20` from 8 control points).

## Optional Task Status

- Optional separate multi-elevate button: not implemented.
- Optional dynamic stop criterion: not implemented.
- Optional draggable control points: not implemented.

## Validation Summary

- `javac *.java`: pass.
- `java BezierCliTest sample.data 1`: pass (`ELEVATED_NPOINTS=9`).
- `java BezierCliTest sample.data 5`: pass (`ELEVATED_NPOINTS=13`).
- `java BezierCliTest sample.data 12`: pass (`ELEVATED_NPOINTS=20`).
- `java BezierCliTest sample.data 0`: pass (`ELEVATED_NPOINTS=0`).
- Negative tests:
  - missing args prints usage and exits non-zero,
  - negative elevation count prints error and exits with status 2.
