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

package heroesgrave.spade.experimental.exporters;

import heroesgrave.spade.gui.dialogs.ProgressDialog;
import heroesgrave.spade.image.Document;
import heroesgrave.spade.io.ImageExporter;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * BIN Image Exporter.
 * 
 * Format is as follows:
 * signed_int32 width
 * signed_int32 height
 * INT_RGBA[width*height] imageData
 * 
 * @author Longor1996
 **/
public class ExporterBIN extends ImageExporter
{
	@Override
	public String getFileExtension()
	{
		return "bin";
	}
	
	@Override
	public void save(Document doc, File destination) throws IOException
	{
		BufferedImage image = doc.getRenderedImage();
		
		DataOutputStream output = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(destination)));
		
		// Write width and height as int32 (Signed 32-Bit Integer).
		output.writeInt(doc.getWidth());
		output.writeInt(doc.getHeight());
		
		// Buffer the Image-Data
		int[] buf = new int[image.getWidth() * image.getHeight()];
		byte[] abyte0 = new byte[image.getWidth() * image.getHeight() * 4];
		
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), buf, 0, image.getWidth());
		
		ProgressDialog DIALOG = new ProgressDialog("Saving...", "Saving Image...", buf.length + 1);
		
		// Go through ALL the pixels and convert from INT_ARGB to INT_RGBA
		
		// FIXME: I suspect the JVM is making crappy optimisations here that cause major issues.
		// A workaround is to print the value of k each iteration, but that is slow.
		// Without the print statement, k sometime managed to hit negative values on a 512x512 image, causing OOB errors.
		for(int k = 0; k < buf.length; k++)
		{
			int A = (buf[k] >> 24) & 0xff;
			int R = (buf[k] >> 16) & 0xff;
			int G = (buf[k] >> 8) & 0xff;
			int B = buf[k] & 0xff;
			
			abyte0[(k << 2) + 0] = (byte) R; // R
			abyte0[(k << 2) + 1] = (byte) G; // G
			abyte0[(k << 2) + 2] = (byte) B; // B
			abyte0[(k << 2) + 3] = (byte) A; // A
			
			if(k % 128 == 0)
			{
				DIALOG.setValue(k);
			}
		}
		
		// Write image as INT_RGBA
		output.write(abyte0);
		
		// Write End Sequence
		output.write('E');
		output.write('O');
		output.write('I');
		output.write('D');
		
		// Done!
		output.flush();
		output.close();
		DIALOG.close();
		buf = null;
		abyte0 = null;
	}
	
	@Override
	public String getDescription()
	{
		return "BIN - Raw Binary Image Data Format";
	}
}
