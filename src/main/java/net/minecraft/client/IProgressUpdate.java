package net.minecraft.client;

public interface IProgressUpdate {
    void displayProgressMessage(String var1);

    void displayLoadingString(String var1);

    void setLoadingProgress(int var1);
}
