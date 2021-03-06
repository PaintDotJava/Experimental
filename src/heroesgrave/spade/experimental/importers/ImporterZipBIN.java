// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Spade developers.
 * 
 * This file is part of Spade
 * 
 * Spade is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package heroesgrave.spade.experimental.importers;

import heroesgrave.spade.gui.dialogs.ProgressDialog;
import heroesgrave.spade.image.Document;
import heroesgrave.spade.image.Layer;
import heroesgrave.spade.image.RawImage;
import heroesgrave.spade.io.ImageImporter;
import heroesgrave.utils.misc.Metadata;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

/**
 * @author Longor1996
 **/
public class ImporterZipBIN extends ImageImporter
{
	@Override
	public boolean load(File file, Document doc) throws IOException
	{
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		
		int width = in.readInt();
		int height = in.readInt();
		int surfaceArea = width * height;
		int zipLen = in.readInt();
		int[] raw = new int[surfaceArea];
		byte[] zipd = new byte[zipLen];
		ProgressDialog DIALOG = new ProgressDialog("Working!", "Loading Image...", surfaceArea);
		DIALOG.setMessage("Reading compressed Image-bytes...");
		in.readFully(zipd);
		
		DIALOG.setMessage("Converting to Java2D readable format...");
		
		ByteArrayInputStream inImg = new ByteArrayInputStream(zipd);
		GZIPInputStream inImgZip = new GZIPInputStream(inImg);
		DataInputStream inImgZipData = new DataInputStream(inImgZip);
		
		for(int index = 0, pixel = 0; index < surfaceArea; index++)
		{
			// pixel = RGBA
			pixel = (inImgZipData.read() << 24) | (inImgZipData.read() << 16) | (inImgZipData.read() << 8) | (inImgZipData.read());
			
			// fastest possible conversion from RGBA to ARGB (?)
			int RGB = ((pixel & 0xFFFFFFFF) >> 8) & 0xFFFFFF;
			int A = pixel & 0xFF;
			pixel = (A << 24) | RGB;
			
			// raw[index] = ARGB
			raw[index] = pixel;
			
			if(index % 128 == 0)
			{
				DIALOG.setValue(index);
			}
		}
		
		in.read();///E
		in.read();///O
		in.read();///I
		in.read();///D
		
		DIALOG.setMessage("Done!");
		
		DIALOG.close();
		in.close();
		
		RawImage image = new RawImage(width, height, raw);
		
		doc.setDimensions(width, height);
		doc.setRoot(new Layer(doc, image, new Metadata()));
		return true;
	}
	
	@Override
	public String getFileExtension()
	{
		return "zbin";
	}
	
	@Override
	public String getDescription()
	{
		return "ZBIN - Raw Compressed Binary Image Data Format";
	}
	
}
