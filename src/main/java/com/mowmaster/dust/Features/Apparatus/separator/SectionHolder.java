package com.mowmaster.dust.Features.Apparatus.separator;

// A block entity with a drum section: the Dust Separator and its extensions
public interface SectionHolder
{
    DrumSection section();

    // Saves and tells clients after the section changed
    void sectionChanged();
}
