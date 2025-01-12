package tomlParser;

import arc.files.Fi;
import arc.struct.Seq;
import arc.util.Log;
import mindustry.Vars;
import mindustry.ctype.Content;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;
import mindustry.mod.Mods;
import mindustry.type.ErrorContent;
import org.tomlj.Toml;
import tomlParser.parser.TomlParser;

import java.util.Locale;

import static mindustry.Vars.content;

public class TomlMods {
    private static final TomlParser parser = new TomlParser();

    /** 创建mod文件中的所有内容。 */
    public static void loadContent() {


        content.setCurrentMod(null);

        class LoadRun implements Comparable<LoadRun> {
            final ContentType type;
            final Fi file;
            final Mods.LoadedMod mod;

            public LoadRun(ContentType type, Fi file, Mods.LoadedMod mod) {
                this.type = type;
                this.file = file;
                this.mod = mod;
            }

            @Override
            public int compareTo(LoadRun l) {
                int mod = this.mod.name.compareTo(l.mod.name);
                if (mod != 0) return mod;
                return this.file.name().compareTo(l.file.name());
            }
        }

        Seq<LoadRun> runs = new Seq<>();

        for (Mods.LoadedMod mod : Vars.mods.orderedMods()) {
            if (mod.root.child("contentToml").exists()) {
                Fi contentRoot = mod.root.child("contentToml");
                for (ContentType type : ContentType.all) {
                    String lower = type.name().toLowerCase(Locale.ROOT);

                    Fi folder = contentRoot.child(lower + (lower.endsWith("s") ? "" : "s"));//units,items....

                    if (folder.exists()) {
                        for (Fi file : folder.findAll(f -> f.extension().equals("json") || f.extension().equals("hjson") || f.extension().equals("toml"))) {
                            runs.add(new LoadRun(type, file, mod));
                        }
                    }
                }
            }
        }

        //确保mod内容是在适当的顺序
        runs.sort();
        for (LoadRun l : runs) {
            Content current = content.getLastAdded();
            try {
                //这将绑定内容，但不会完全加载
                Content loaded = parser.parse(l.mod, l.file.nameWithoutExtension(),

                                              l.file.extension().equals("toml")? Toml.parse(l.file.read()).toJson():
                                              l.file.readString("UTF-8")

                        , l.file, l.type);
                Log.debug("[@] Loaded '@'.", l.mod.meta.name, (loaded instanceof UnlockableContent u ? u.localizedName : loaded));
            } catch (Throwable e) {
                if (current != content.getLastAdded() && content.getLastAdded() != null) {
                    parser.markError(content.getLastAdded(), l.mod, l.file, e);
                } else {
                    ErrorContent error = new ErrorContent();
                    parser.markError(error, l.mod, l.file, e);
                }
            }
        }

        //这样就完成了对内容字段的解析
        parser.finishParsing();
    }
}
