package net.minecraft.client;

public interface IProgressUpdate {
    void displayProgressMessage();

    void displayLoadingString(String string1);

    void setLoadingProgress(int i1);
}
