package net.minecraft.client;

import com.mojang.nbt.NBTBase;
import com.mojang.nbt.NBTTagCompound;

import net.lax1dude.eaglercraft.EaglerZLIB;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CompressedStreamTools {
    public static NBTTagCompound readCompressed(InputStream inputStream0) throws IOException {
        DataInputStream dataInputStream4 = new DataInputStream(EaglerZLIB.newGZIPInputStream(inputStream0));

        NBTTagCompound nBTTagCompound5;
        try {
            nBTTagCompound5 = read(dataInputStream4);
        } finally {
            dataInputStream4.close();
        }

        return nBTTagCompound5;
    }

    public static void writeCompressed(NBTTagCompound nbtcomptag, OutputStream outputStream1) throws IOException {
        DataOutputStream dataOutputStream5 = new DataOutputStream(EaglerZLIB.newGZIPOutputStream(outputStream1));

        try {
            write(nbtcomptag, dataOutputStream5);
        } finally {
            dataOutputStream5.close();
        }

    }

    public static NBTTagCompound read(DataInput dataInput0) throws IOException {
        NBTBase nBTBase1 = NBTBase.readNamedTag(dataInput0);
        if(nBTBase1 instanceof NBTTagCompound) {
            return (NBTTagCompound)nBTBase1;
        } else {
            throw new IOException("Root tag must be a named compound tag");
        }
    }

    public static void write(NBTTagCompound nBTTagCompound0, DataOutput dataOutput1) throws IOException {
        NBTBase.writeNamedTag(nBTTagCompound0, dataOutput1);
    }
}
