csp = {
  X = { x1, x2, x3, x4 },
  D = { [1..6], [1..6], [1..6], [1..6] },
  C = {
    x1 != x2,
    x1 != x3,
    x1 != x4,
    x2 != x3,
    x2 != x4,
    x3 != x4,
    x1 != 1,
    x2 != 2,
    x3 != 3,
    x4 != 4,
    x3 <= 5,
    x4 > 4,
    x4 - x3 > 2,
    x3 + x1 >= 5,
    x1 * x2 >= 6,
    x1 + x2 <= 7,
  }
}

#### Unary contraints, node consistent
<(x1), [2,3,4,5,6]>
<(x2), [1,3,4,5,6]>
<(x3), [1,2,4,5]>
<(x4), [5,6]>

#### Binary contraints, arc consistent
<(x1,x2), (x1 != x2) && (x1 * x2 >= 6) && (x1 + x2 <= 7)>
<(x1,x2), [
  (2, 3),
  (2, 4),
  (2, 5),
  (3, 4),
  (4, 3),
  (6, 1),
]>

<(x1,x3), (x1 != x3) && (x3 + x1 >= 5)>
<(x1,x3), [
  (2, 4),
  (2, 5),
  (3, 2),
  (3, 4),
  (3, 5),
  (4, 1),
  (4, 2),
  (4, 5),
  (5, 1),
  (5, 2),
  (5, 4),
  (6, 1),
  (6, 2),
  (6, 4),
  (6, 5),
]>

<(x1,x4), (x1 != x4)>
<(x1,x4), [
  (2, 5),
  (2, 6),
  (3, 5),
  (3, 6),
  (4, 5),
  (4, 6),
  (5, 6),
  (6, 5),
]>

<(x2,x3), (x2 != x3)>
<(x2,x3), [
  (1, 2),
  (1, 4),
  (1, 5),
  (3, 1),
  (3, 2),
  (3, 4),
  (3, 5),
  (4, 1),
  (4, 2),
  (4, 5),
  (5, 1),
  (5, 2),
  (5, 4),
  (6, 1),
  (6, 2),
  (6, 4),
  (6, 5),
]>

<(x2,x4), (x2 != x4)>
<(x2,x4), [
  (1, 5),
  (1, 6),
  (3, 5),
  (3, 6),
  (4, 5),
  (4, 6),
  (5, 6),
  (6, 5),
]>

<(x3,x4), (x3 != x4) && (x4 - x3 > 2)>
<(x3,x4), [
  (1, 5),
  (1, 6),
  (2, 5),
  (2, 6),
]>