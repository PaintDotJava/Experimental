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

package heroesgrave.spade.experimental;

import heroesgrave.spade.experimental.exporters.ExporterBIN;
import heroesgrave.spade.experimental.exporters.ExporterZipBIN;
import heroesgrave.spade.experimental.importers.ImporterBIN;
import heroesgrave.spade.experimental.importers.ImporterZipBIN;
import heroesgrave.spade.plugin.Plugin;
import heroesgrave.spade.plugin.Registrar;

public class ExperimentalPlugin extends Plugin
{
	public static void main(String[] args)
	{
		launchSpadeWithPlugins(args, true, new ExperimentalPlugin());
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
		
		registrar.registerImporter(new ImporterBIN());
		registrar.registerImporter(new ImporterZipBIN());
	}
}
