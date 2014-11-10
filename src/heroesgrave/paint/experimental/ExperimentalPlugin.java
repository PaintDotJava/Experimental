package heroesgrave.paint.experimental;

import heroesgrave.paint.experimental.exporters.ExporterBIN;
import heroesgrave.paint.experimental.exporters.ExporterPDJ;
import heroesgrave.paint.experimental.exporters.ExporterZipBIN;
import heroesgrave.paint.experimental.importers.ImporterBIN;
import heroesgrave.paint.experimental.importers.ImporterPDJ;
import heroesgrave.paint.experimental.importers.ImporterZipBIN;
import heroesgrave.paint.plugin.Plugin;
import heroesgrave.paint.plugin.Registrar;

public class ExperimentalPlugin extends Plugin
{
	public static void main(String[] args)
	{
		launchPaintWithPlugins(args, true, new ExperimentalPlugin());
	}
	
	@Override
	public void load()
	{
		
	}
	
	@Override
	public void register(Registrar registrar)
	{
		registrar.registerExporter(new ExporterBIN());
		registrar.registerExporter(new ExporterZipBIN());
		registrar.registerExporter(new ExporterPDJ());
		
		registrar.registerImporter(new ImporterBIN());
		registrar.registerImporter(new ImporterZipBIN());
		registrar.registerImporter(new ImporterPDJ());
	}
}
