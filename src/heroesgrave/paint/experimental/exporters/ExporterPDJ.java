package heroesgrave.paint.experimental.exporters;

import heroesgrave.paint.gui.SimpleModalProgressDialog;
import heroesgrave.paint.image.Document;
import heroesgrave.paint.image.Layer;
import heroesgrave.paint.image.RawImage;
import heroesgrave.paint.io.ImageExporter;
import heroesgrave.utils.misc.Metadata;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;

public class ExporterPDJ extends ImageExporter
{
	@Override
	public String getFileExtension()
	{
		return "pdj";
	}
	
	@Override
	public String getFileExtensionDescription()
	{
		return "PDJ - Paint.JAVA experimental file format (supports layers)";
	}
	
	@Override
	public void export(Document doc, File destination) throws IOException
	{
		GZIPOutputStream zip = new GZIPOutputStream(new BufferedOutputStream(new FileOutputStream(destination)));
		DataOutputStream out = new DataOutputStream(zip);
		
		out.writeInt(doc.getWidth());
		out.writeInt(doc.getHeight());
		out.writeInt(doc.getFlatMap().size());
		
		writeMetadata(out, doc.getMetadata());
		
		int c = doc.getWidth() * doc.getHeight() * doc.getFlatMap().size();
		SimpleModalProgressDialog dialog = new SimpleModalProgressDialog("Saving...", "Saving Image...", c + 1);
		
		writeLayer(out, doc.getRoot(), dialog);
		out.flush();
		dialog.setMessage("Compressing...");
		zip.finish();
		zip.flush();
		out.close();
		
		dialog.close();
	}
	
	public void writeLayer(DataOutputStream out, Layer layer, SimpleModalProgressDialog dialog) throws IOException
	{
		writeMetadata(out, layer.getMetadata());
		
		RawImage image = layer.getImage();
		int[] buffer = image.borrowBuffer();
		
		int start = dialog.getValue();
		
		// Why does java not have a nice way to write an int[]?
		// Or at least a way to convert an int[] to a byte[] for I/O?
		for(int i = 0; i < buffer.length; i++)
		{
			out.writeInt(buffer[i]);
			
			if(i % 128 == 0)
			{
				dialog.setValue(start + i);
			}
		}
		dialog.setValue(start + buffer.length);
		
		int count = layer.getChildCount();
		
		out.writeInt(count);
		
		for(int i = 0; i < count; i++)
		{
			writeLayer(out, (Layer) layer.getChildAt(i), dialog);
		}
	}
	
	public void writeMetadata(DataOutputStream out, Metadata data) throws IOException
	{
		out.writeInt(data.size());
		for(Entry<String, String> e : data.getEntries())
		{
			out.writeUTF(e.getKey());
			out.writeUTF(e.getValue());
		}
	}
}
