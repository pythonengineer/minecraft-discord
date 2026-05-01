package net.minecraft.client;

import com.mojang.nbt.NBTBase;
import com.mojang.nbt.NBTTagCompound;

import net.lax1dude.eaglercraft.EaglerZLIB;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class UnexpectedThrowable {
    public final Exception exception;

    public UnexpectedThrowable(String string1, Exception exception2) {
        this.exception = exception2;
    }

    public static NBTTagCompound readCompressed(InputStream inputStream0) throws IOException {
        DataInputStream dataInputStream4 = new DataInputStream(EaglerZLIB.newGZIPInputStream(inputStream0));

        NBTTagCompound nBTTagCompound5;
        try {
            NBTBase nBTBase1;
            if(!((nBTBase1 = NBTBase.readNamedTag(dataInputStream4)) instanceof NBTTagCompound)) {
                throw new IOException("Root tag must be a named compound tag");
            }

            nBTTagCompound5 = (NBTTagCompound)nBTBase1;
        } finally {
            dataInputStream4.close();
        }

        return nBTTagCompound5;
    }

    public static void writeCompressed(NBTTagCompound nBTTagCompound0, OutputStream outputStream1) throws IOException {
        DataOutputStream dataOutputStream5 = new DataOutputStream(EaglerZLIB.newGZIPOutputStream(outputStream1));

        try {
            NBTBase.writeNamedTag(nBTTagCompound0, dataOutputStream5);
        } finally {
            dataOutputStream5.close();
        }

    }
}
