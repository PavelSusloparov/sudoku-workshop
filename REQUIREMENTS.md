# Requirements

## Feature 1

As a projectors hardware engineer I would like to have ability to calculate projector angle based on the building height and distance to the building so I can configure the projector

Acceptance criteria:
- Rest API JSON interface
- Given total height of the wall, floor adjustment and distance to the wall given in inches
- Height of the wall, distance to the wall, floor adjustment is Integer
- Projector upper angle must be 18 inches lower then the upper point of the wall. If wall total height is less then 18 inches, angle should be 0.
- Calculation results should be reported on. Reporting is based on Height of the building, distance to the building and projector angle

## Feature 2

As a projector hardware engineer I would like to have ability to specify height of the wall and distance to the wall with a floating point so I can increase the projector precision

Acceptance criteria:
- Height of the wall and distance to the wall can have a floating point

[Visualization](https://drive.google.com/drive/folders/1Hqj7idDcsRBgANXqGcKZxBIARErUxz0J)
