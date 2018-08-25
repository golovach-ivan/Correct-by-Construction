nominal calculi—computational formalisms that include dynamic name generation

The [pi-calculus](???) is the original example of a nominal calculus, and is the archetype for many others.

**A pure name** to be “nothing but a bit pattern that is an identifier, and is only useful for comparing for identity with other bit patterns — which includes looking up in tables in order to find other information”. An example of a pure name is the 128-bit GUID.

**An impure name** is one with some kind of recognisable structure, such as a file path or a URL containing a path. An impure name does more than simply name a single object. For example, the file name *rmn/animals/pig* may imply the presence of a directory *rmn*
and a subdirectory *animals*.

Example: [the pi calculus](???), [the spi calculus](???), [the ambient calculus](???).

Freshness.
