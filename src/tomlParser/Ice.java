package tomlParser;

import mindustry.mod.Mod;

public class Ice extends Mod {
    public Ice() {}

    @Override
    public void loadContent() {
        TomlMods.loadContent();
    }
}
