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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

public class ImporterPDJ extends ImageImporter
{
	@Override
	public void read(File file, Document doc) throws IOException
	{
		GZIPInputStream zip = new GZIPInputStream(new BufferedInputStream(new FileInputStream(file)));
		DataInputStream in = new DataInputStream(zip);
		
		int width = in.readInt();
		int height = in.readInt();
		int layerCount = in.readInt();
		
		doc.setDimensions(width, height);
		
		doc.setMetadata(readMetadata(in));
		
		int c = doc.getWidth() * doc.getHeight() * layerCount;
		ProgressDialog dialog = new ProgressDialog("Loading...", "Loading Image...", c + 2);
		
		doc.setRoot(readLayer(in, doc, dialog));
		
		dialog.close();
	}
	
	private Layer readLayer(DataInputStream in, Document doc, ProgressDialog dialog) throws IOException
	{
		Metadata info = readMetadata(in);
		int[] buffer = new int[doc.getWidth() * doc.getHeight()];
		int start = dialog.getValue();
		for(int i = 0; i < buffer.length; i++)
		{
			buffer[i] = in.readInt();
			if(i % 128 == 0)
			{
				dialog.setValue(start + i);
			}
		}
		dialog.setValue(start + buffer.length);
		RawImage image = new RawImage(doc.getWidth(), doc.getHeight(), buffer);
		Layer layer = new Layer(doc, image, info);
		
		int children = in.readInt();
		for(int i = 0; i < children; i++)
		{
			layer.addLayer(readLayer(in, doc, dialog));
		}
		
		return layer;
	}
	
	private Metadata readMetadata(DataInputStream in) throws IOException
	{
		int count = in.readInt();
		Metadata info = new Metadata();
		for(int i = 0; i < count; i++)
		{
			info.set(in.readUTF(), in.readUTF());
		}
		return info;
	}
	
	@Override
	public String getFormat()
	{
		return "pdj";
	}
	
	@Override
	public String getDescription()
	{
		return "PDJ - Paint.JAVA experimental file format (supports layers)";
	}
}
