/*
 * JasperReports - Free Java Reporting Library.
 * Copyright (C) 2001 - 2011 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 *
 * Unless you have purchased a commercial license agreement from Jaspersoft,
 * the following license terms apply:
 *
 * This program is part of JasperReports.
 *
 * JasperReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JasperReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JasperReports. If not, see <http://www.gnu.org/licenses/>.
 */
package net.sf.jasperreports.compilers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRRuntimeException;

/**
 * Compiled Java code for reports that use JavaScript as expression language.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JavaScriptCompiledData.java 5451 2012-06-14 15:35:10Z lucianc $
 * @see JavaScriptClassCompiler
 */
public class JavaScriptCompiledData implements Serializable
{

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	protected static class ExpressionIndexes implements Serializable
	{
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

		private final int defaultExpressionIdx;
		private final int oldExpressionIdx;
		private final int estimatedExpressionIdx;

		public ExpressionIndexes(int defaultExpressionIdx, int oldExpressionIdx, int estimatedExpressionIdx)
		{
			this.defaultExpressionIdx = defaultExpressionIdx;
			this.oldExpressionIdx = oldExpressionIdx;
			this.estimatedExpressionIdx = estimatedExpressionIdx;
		}

		public int getDefaultExpressionIndex()
		{
			return defaultExpressionIdx;
		}

		public int getOldExpressionIndex()
		{
			return oldExpressionIdx;
		}

		public int getEstimatedExpressionIndex()
		{
			return estimatedExpressionIdx;
		}
	}
	
	protected static class CompiledClass implements Serializable
	{
		private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

		private final String className;
		private final byte[] classBytes;
		
		public CompiledClass(String className, byte[] classBytes)
		{
			this.className = className;
			this.classBytes = classBytes;
		}

		public String getClassName()
		{
			return className;
		}

		public byte[] getClassBytes()
		{
			return classBytes;
		}
	}
	
	protected static int makeExpressionIndex(int scriptIndex, int expressionId)
	{
		if (scriptIndex > 0x7fff || expressionId > 0x7fff)
		{
			throw new JRRuntimeException("Too many expressions in report");
		}
		
		return ((scriptIndex & 0x7fff) << 16) | (expressionId & 0x7fff);
	}
	
	protected static int scriptIndex(int expressionIndex)
	{
		return (expressionIndex >> 16) & 0x7fff;
	}
	
	protected static int expressionId(int expressionIndex)
	{
		return expressionIndex & 0x7fff;
	}
	
	private final List<ExpressionIndexes> expressionIndexes = new ArrayList<ExpressionIndexes>();
	private final List<CompiledClass> compiledClasses = new ArrayList<CompiledClass>(1);
	
	public void addExpression(int expressionId, 
			int defaultExpressionIdx, int oldExpressionIdx, int estimatedExpressionIdx)
	{
		for (int idx = expressionIndexes.size(); idx <= expressionId; ++idx)
		{
			expressionIndexes.add(idx, null);
		}

		ExpressionIndexes expressionData = new ExpressionIndexes(defaultExpressionIdx, oldExpressionIdx, estimatedExpressionIdx);
		expressionIndexes.set(expressionId, expressionData);
	}
	
	public ExpressionIndexes getExpression(int id)
	{
		if (id >= expressionIndexes.size())
		{
			throw new JRRuntimeException("No expression for id " + id);
		}
		ExpressionIndexes expr = expressionIndexes.get(id);
		if (expr == null)
		{
			throw new JRRuntimeException("No expression for id " + id);
		}
		return expr;
	}

	public void addClass(String className, byte[] data)
	{
		compiledClasses.add(new CompiledClass(className, data));
	}
	
	public CompiledClass getCompiledClass(int classIndex)
	{
		return compiledClasses.get(classIndex);
	}
}
