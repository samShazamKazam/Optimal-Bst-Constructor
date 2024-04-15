# OptimalBstConstructor
Optimal Binary Search Tree Constructor

## Problem
Given a set of keys and their probabilities, construct a binary search tree 
with the goal to minimize the average search time for keys in the tree.

```
search time for a key = depth of the key
search time for a tree = SUM(for k=1..n , the probability for k * search time for k)
```

## Context
Knuth's Optimum Binary Search Tree algorithm 
(Optimum Binary Search Trees 1971) is a dynamic 
programming algorithm used to construct 
an optimal binary search tree given a set of keys 
and their associated probabilities. The goal is to 
minimize the average search time for keys in the tree.
