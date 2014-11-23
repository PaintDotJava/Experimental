// {LICENSE}
/*
 * Copyright 2013-2014 HeroesGrave and other Paint.JAVA developers.
 * 
 * This file is part of Paint.JAVA
 * 
 * Paint.JAVA is free software: you can redistribute it and/or modify
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

package heroesgrave.paint.experimental.importers;

import heroesgrave.paint.gui.ProgressDialog;
import heroesgrave.paint.image.Document;
import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.RawImage;
import heroesgrave.paint.io.ImageImporter;
import heroesgrave.utils.misc.Metadata;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Longor1996
 **/
public class ImporterBIN extends ImageImporter
{
	@Override
	public void load(File file, Document doc) throws IOException
	{
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		
		int width = in.readInt();
		int height = in.readInt();
		int surfaceArea = width * height;
		int[] raw = new int[surfaceArea];
		
		ProgressDialog DIALOG = new ProgressDialog("Working!", "Loading Image...", surfaceArea);
		
		for(int I = 0; I < surfaceArea; I++)
		{
			// get
			int pixel = in.readInt();
			
			// convert
			int R = (pixel >> 24) & 0xff;
			int G = (pixel >> 16) & 0xff;
			int B = (pixel >> 8) & 0xff;
			int A = (pixel) & 0xff;
			
			pixel = 0;
			pixel |= B;
			pixel |= G << 8;
			pixel |= R << 16;
			pixel |= A << 24;
			
			// put
			raw[I] = pixel;
			
			// Don't update the progress-bar for every value, since that can cause some serious slowdown!
			if(I % 128 == 0)
			{
				DIALOG.setValue(I);
			}
		}
		
		// set progress to 100
		DIALOG.setValue(surfaceArea - 1);
		
		// close progress dialog
		DIALOG.close();
		
		// Read 'EOID'
		in.readInt();
		
		in.close();
		
		RawImage image = new RawImage(width, height, raw);
		
		doc.setDimensions(width, height);
		doc.setRoot(new Layer(doc, image, new Metadata()));
	}
	
	@Override
	public String getFileExtension()
	{
		return "bin";
	}
	
	@Override
	public String getDescription()
	{
		return "BIN - Raw Binary Image Data Format";
	}
}
