package com.mowmaster.dust.Features.Guide;

// A block entity whose moving parts play out on the client between updates from the server: it counts the ticks there
// itself (how far into a phase it is, how far a part has turned). A guide scene shows one running: it makes the block
// entity from the block and its "data" (GuideScene, GuideStep), then calls guideTick once for every tick since the
// scene set it up, and draws it with its block entity renderer (Client/GuideSceneRenderer).
// guideTick only moves what's drawn. A block entity in a guide has no level: no world, particles or sounds.
public interface GuideAnimated
{
    void guideTick();
}
