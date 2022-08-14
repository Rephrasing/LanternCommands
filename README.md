# LanternCommands
a powerful framework for Bukkit commands (1.8.8-1.19)

# implementation!

Maven:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.Rephrasing</groupId>
    <artifactId>LanternCommands</artifactId>
    <version>master-1bed3fe460-1</version>
</dependency>
```

Gradle (Kotlin):
```kt
repositories {
    maven {
        url = uri("https://jitpack.io")
    }
}

dependencies {
    implementation("com.github.Rephrasing:LanternCommands:master-1bed3fe460-1")
}

```

Gradle:
```gradle
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
        implementation 'com.github.Rephrasing:LanternCommands:master-1bed3fe460-1'
}
```


# usage!
There are two ways of implementing your own LanternCommand. Choose whichever feels comfortable to use.</p>
However, Only one way to register them!

```java
import com.github.rephrasing.lantern.common.LanternCommand;
import com.github.rephrasing.lantern.common.LanternExecutionContext;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class TestCommand extends LanternCommand {

    public TestCommand() {
        super(JavaPlugin.getPlugin(MyPlugin.class), "ImplementationTest");
        setCooldown(20);
        setPermissionCheck(context -> context.getSender().asPlayer().isOp());
        setCooldownMessage(ChatColor.RED + "You cannot use this command for" + ChatColor.YELLOW + "%seconds%" + ChatColor.RED + "seconds");
        setPlayerOnly(true);
    }

    @Override
    public boolean execute(LanternExecutionContext context) {
        context.getSender().getInstance().sendMessage("Implementation Test executed");
        return true;
    }

    @Override
    public List<String> tabComplete(LanternExecutionContext context, String argUsed, int argSize) {
        if (argSize == 1) return List.of("hi", "hello");
        return null;
    }
}

```

```java
import com.github.rephrasing.lantern.builder.LanternCommandBuilder;
import com.github.rephrasing.lantern.internal.LanternCommandAdapter;
import com.github.rephrasing.lantern.common.LanternCommand;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class MyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        
        LanternCommand build = LanternCommandBuilder.builder(this, "LanternBuilderTest")
                .execution(context -> {
                    context.getSender().getInstance().sendMessage("BuilderTest executed!");
                    return true;
                }
                ).tabComplete((context, argUsed, argSize) -> {
                    if (argSize == 1) return List.of("hello", "hi");
                    return null;
                }
                ).playerOnly(true)
                .cooldown(20)
                .permissionCheck(context -> context.getSender().asPlayer().isOp())
                .cooldownMessage(ChatColor.RED + "You cannot use this command for" + ChatColor.YELLOW + "%seconds%" + ChatColor.RED + "seconds")
                .build();
        
        LanternCommandAdapter.registerCommand(build);
        LanternCommandAdapter.registerCommand(new TestCommand());
    }
}
```
