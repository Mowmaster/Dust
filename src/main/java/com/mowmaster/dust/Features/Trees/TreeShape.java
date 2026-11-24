package com.mowmaster.dust.Features.Trees;

// The shapes the elemental trees grow in: vanilla's own trunk and foliage placers with vanilla's numbers (DustWorldgen
// builds each wood's tree from its shape; the 26.1.2 copies are in src/main/resources-26.1.2/.../configured_feature)
//   OAK     straight trunk 5 + 0-2, a blob of leaves (oak)
//   TALL    straight trunk 6 + 0-3, a blob of leaves (a tall birch)
//   FANCY   fancy oak's branching trunk and leaf balls
//   ACACIA  acacia's forking trunk and flat canopies
//   SPRUCE  spruce's straight trunk and layered cone
//   PINE    pine's tall trunk and narrow top
//   AZALEA  azalea's bending trunk and scattered leaves
public enum TreeShape
{
    OAK, TALL, FANCY, ACACIA, SPRUCE, PINE, AZALEA
}
