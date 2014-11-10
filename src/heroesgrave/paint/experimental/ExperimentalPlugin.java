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
