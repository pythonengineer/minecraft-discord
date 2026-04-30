package net.minecraft.client;

import com.mojang.nbt.NBTBase;
import com.mojang.nbt.NBTTagCompound;

import net.lax1dude.eaglercraft.EaglerZLIB;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
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

        NBTTagCompound nBTTagCompound1;
        try {
            nBTTagCompound1 = read(dataInputStream4);
        } finally {
            dataInputStream4.close();
        }

        return nBTTagCompound1;
    }

    public static void writeCompressed(NBTTagCompound nBTTagCompound0, OutputStream outputStream1) throws IOException {
        DataOutputStream dataOutputStream5 = new DataOutputStream(EaglerZLIB.newGZIPOutputStream(outputStream1));

        try {
            NBTBase.writeNamedTag(nBTTagCompound0, dataOutputStream5);
        } finally {
            dataOutputStream5.close();
        }

    }

    public static NBTTagCompound decompress(byte[] b0) throws IOException {
        DataInputStream dataInputStream4 = new DataInputStream(EaglerZLIB.newGZIPInputStream(new ByteArrayInputStream(b0)));

        NBTTagCompound nBTTagCompound1;
        try {
            nBTTagCompound1 = read(dataInputStream4);
        } finally {
            dataInputStream4.close();
        }

        return nBTTagCompound1;
    }

    public static byte[] compress(NBTTagCompound nBTTagCompound0) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream2 = new DataOutputStream(EaglerZLIB.newGZIPOutputStream(byteArrayOutputStream1));

        try {
            NBTBase.writeNamedTag(nBTTagCompound0, dataOutputStream2);
        } finally {
            dataOutputStream2.close();
        }

        return byteArrayOutputStream1.toByteArray();
    }

    public static NBTTagCompound read(DataInput dataInput0) throws IOException {
        NBTBase nBTBase1;
        if((nBTBase1 = NBTBase.readNamedTag(dataInput0)) instanceof NBTTagCompound) {
            return (NBTTagCompound)nBTBase1;
        } else {
            throw new IOException("Root tag must be a named compound tag");
        }
    }
}
