/*
Copyright (C) 2001 Chr. Clemens Lee <clemens@kclee.com>.

This file is part of JavaNCSS 
(http://www.kclee.com/clemens/java/javancss/).

JavaNCSS is free software; you can redistribute it and/or modify it
under the terms of the GNU General Public License as published by the
Free Software Foundation; either version 2, or (at your option) any
later version.

JavaNCSS is distributed in the hope that it will be useful, but WITHOUT
ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
for more details.

You should have received a copy of the GNU General Public License
along with JavaNCSS; see the file COPYING.  If not, write to
the Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA 02111-1307, USA.  */

package javancss;

import ccl.util.Util;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;

/**
 * Generates ascii output of Java metrics.
 *
 * @author    Chr. Clemens Lee <clemens@kclee.com>
 *            , Windows 13 10 line feed feature by John Wilson.
 * @version   $Id: AsciiFormatter.java,v 1.7 2006/04/16 11:42:17 clemens Exp clemens $
 */
public class AsciiFormatter implements Formatter
				       , JavancssConstants
{
    static final int LEN_NR = 3;
    private static final String NL = System.getProperty("line.separator");
    /*private static final String NL = "\r\n";*/

    private Javancss _javancss = null;

    private String[] _header = null;
    private int      _length = 0;
    private int      _nr     = 0;

    static NumberFormat _pNumberFormat = null;

    private String _formatListHeader( int lines, String[] header )
    {
	_header = header;

	_nr = 0;

	StringBuffer sRetVal = new StringBuffer();

        _length = Util.itoa( lines ).length();
        int spaces = Math.max( 0, _length - LEN_NR );
        _length = spaces + LEN_NR;
        sRetVal.append( Util.multiplyChar(' ', spaces) );
	sRetVal.append( "Nr." );
	for( int nr = 0; nr < header.length; nr++ )
	{
	    sRetVal.append( " " ).append( header[ nr ] );
	}
	sRetVal.append( NL );

	return sRetVal.toString();
    }

    StringBuffer _line = new StringBuffer();

    private String _formatListLine( String name, int[] value )
    {
	StringBuffer sLine = new StringBuffer();

	_nr++;
	sLine.append( Util.paddWithSpace( _nr, _length ) );
	for( int index = 0; index < _header.length - 1; index++ )
	{
	    sLine.append( " " );
	    sLine.append( Util.paddWithSpace( value[ index ]
					      , _header[ index ].length() ) );
	}
	sLine.append( " " );
	sLine.append( name );
	sLine.append( NL );

	return sLine.toString();
    }

    private double _divide( int divident, int divisor )
    {
        double dRetVal = 0.0;
        if ( divisor > 0) {
            dRetVal = Math.round(((double)divident/(double)divisor)*100)/100.0;
        }

	return dRetVal;
    }

    private double _divide( long divident, long divisor )
    {
        double dRetVal = 0.0;
        if ( divisor > 0) {
            dRetVal = Math.round(((double)divident/(double)divisor)*100)/100.0;
        }

	return dRetVal;
    }

    private String _formatPackageMatrix( int packages
					 , int classesSum
					 , int functionsSum
                                         , int javadocsSum
					 , int ncssSum      )
    {
        ((DecimalFormat)_pNumberFormat).applyPattern( "###0.00" );
        int maxItemLength = _pNumberFormat.format(ncssSum).length();
        maxItemLength = Math.max(9, maxItemLength);
        String sRetVal = Util.paddWithSpace( "Packages"
					     , maxItemLength ) 
	    + " " 
	    + Util.paddWithSpace("Classes", maxItemLength) 
	    + " " 
	    + Util.paddWithSpace("Functions", maxItemLength) 
	    + " " 
	    + Util.paddWithSpace("NCSS", maxItemLength) 
	    + " " 
	    + Util.paddWithSpace("Javadocs", maxItemLength) 
	    + " | per" + NL;

        sRetVal += Util.multiplyChar( '-', (maxItemLength + 1)*6 + 1 )
	    + NL
	    + Util.paddWithSpace(_pNumberFormat.format
                                      (packages), maxItemLength) + " " +
               Util.paddWithSpace(_pNumberFormat.format
                                  (classesSum), maxItemLength) + " " +
               Util.paddWithSpace(_pNumberFormat.format
                                  (functionsSum), maxItemLength) + " " +
               Util.paddWithSpace(_pNumberFormat.format
                                  (ncssSum), maxItemLength) + " " +
               Util.paddWithSpace(_pNumberFormat.format
                                  (javadocsSum), maxItemLength) + " | Project" + NL;


        sRetVal += Util.multiplyChar( ' ', maxItemLength + 1 ) 
	    + Util.paddWithSpace( _pNumberFormat.format( _divide( classesSum, packages ) )
				  , maxItemLength )
	    + " "
	    + Util.paddWithSpace( _pNumberFormat.format( _divide( functionsSum, packages ) )
				  , maxItemLength )
	    + " " 
	    + Util.paddWithSpace( _pNumberFormat.format( _divide( ncssSum, packages ) )
				  , maxItemLength )
	    + " " 
	    + Util.paddWithSpace( _pNumberFormat.format( _divide( javadocsSum, packages ) )
				  , maxItemLength )
	    + " | Package" + NL;

        sRetVal += Util.multiplyChar( ' ', (maxItemLength + 1)*2 ) 
	    + Util.paddWithSpace( _pNumberFormat.format( _divide( functionsSum, classesSum ) )
                                  , maxItemLength ) 
	    + " " 
	    + Util.paddWithSpace( _pNumberFormat.format( _divide( ncssSum, classesSum ) )
				  , maxItemLength )
	    + " " 
	    + Util.paddWithSpace( _pNumberFormat.format( _divide( javadocsSum, classesSum ) )
                                  , maxItemLength ) 
	    + " | Class" + NL;
	
	sRetVal += Util.multiplyChar( ' ', (maxItemLength + 1)*3 )
               + Util.paddWithSpace( _pNumberFormat.format( _divide( ncssSum, functionsSum ) )
                                     , maxItemLength )
               + " "
               + Util.paddWithSpace( _pNumberFormat.format( _divide( javadocsSum, functionsSum ) )
				  , maxItemLength )
               + " | Function" + NL;
        ((DecimalFormat)_pNumberFormat).applyPattern( "#,##0.00" );
        
	return sRetVal;
    }

    public AsciiFormatter( Javancss javancss )
    {
	super();

	_javancss = javancss;

        _pNumberFormat = NumberFormat.getInstance( Locale.US );
        ((DecimalFormat)_pNumberFormat).applyPattern( "#,##0.00" );
    }

    public String printPackageNcss() 
    {
	Vector vPackageMetrics = _javancss.getPackageMetrics();

        int packages = vPackageMetrics.size();

        String sRetVal = _formatListHeader( packages
					    , new String[] {   "  Classes"
							     , "Functions"
						             , "     NCSS"
							     , " Javadocs"
						             , "Package" } );

        int classesSum   = 0;
        int functionsSum = 0;
        int javadocsSum  = 0;
        int ncssSum      = 0;
        for( Enumeration ePackages = vPackageMetrics.elements()
	     ; ePackages.hasMoreElements()
	     ; )
        {
            PackageMetric pPackageMetric = (PackageMetric)ePackages.nextElement();

            classesSum   += pPackageMetric.classes;
            functionsSum += pPackageMetric.functions;
            ncssSum      += pPackageMetric.ncss;
            javadocsSum  += pPackageMetric.javadocs;
	    sRetVal += _formatListLine( pPackageMetric.name
					, new int[] { pPackageMetric.classes
						      , pPackageMetric.functions
						      , pPackageMetric.ncss
                                                      , pPackageMetric.javadocs
                                        } );
        }

	int packagesLength = Util.itoa( packages ).length();
	int spaces = Math.max( packagesLength, LEN_NR ) + 1;
        sRetVal += Util.multiplyChar
               (' ', spaces ) +
               "--------- --------- --------- ---------" + NL;

        sRetVal += Util.multiplyChar(' ', spaces ) 
	    + Util.paddWithSpace( classesSum, 9 )
	    + " "
	    + Util.paddWithSpace( functionsSum, 9 )
	    + " "
	    + Util.paddWithSpace( ncssSum, 9 )
	    + " "
	    + Util.paddWithSpace( javadocsSum, 9 )
	    + " Total" + NL + NL;

	sRetVal += _formatPackageMatrix( packages
					 , classesSum
					 , functionsSum
                                         , javadocsSum
					 , ncssSum      );

        return sRetVal;
    }

    private String _formatObjectResume( int objects
					, long lObjectSum
					, long lFunctionSum
					, long lClassesSum
					, long lJVDCSum     )
    {
        double fAverageNcss     = _divide( lObjectSum  , objects );
        double fAverageFuncs    = _divide( lFunctionSum, objects );
        double fAverageClasses  = _divide( lClassesSum , objects );
        double fAverageJavadocs = _divide( lJVDCSum    , objects );
        NumberFormat pNumberFormat = new DecimalFormat("#,##0.00");
        String sRetVal = "Average Object NCSS:             " +
               Util.paddWithSpace(pNumberFormat.format
                                  (fAverageNcss),     9) + NL;
        sRetVal += "Average Object Functions:        " +
               Util.paddWithSpace(pNumberFormat.format
                                  (fAverageFuncs),    9) + NL;
        sRetVal += "Average Object Inner Classes:    " +
               Util.paddWithSpace(pNumberFormat.format
                                  (fAverageClasses),  9) + NL;
        sRetVal += "Average Object Javadoc Comments: " +
               Util.paddWithSpace(pNumberFormat.format
                                  (fAverageJavadocs), 9) + NL;
        sRetVal += "Program NCSS:                    " +
               Util.paddWithSpace(pNumberFormat.format
				  (_javancss.getNcss()), 9) + NL;
	
	return sRetVal;
    }

    public String printObjectNcss() {
	Vector vObjectMetrics = _javancss.getObjectMetrics();

        String sRetVal = _formatListHeader( vObjectMetrics.size()
					    , new String[] { "NCSS"
							     , "Functions"
							     , "Classes"
							     , "Javadocs"
							     , "Class"     } );
        long lFunctionSum = 0;
        long lClassesSum  = 0;
        long lObjectSum   = 0;
        long lJVDCSum     = 0;
        for( Enumeration eClasses = vObjectMetrics.elements()
	     ; eClasses.hasMoreElements()
	     ; )
        {
            Vector vClassMetrics = (Vector)eClasses.nextElement();
            String sClass = (String)vClassMetrics.elementAt(OBJ_NAME);
            int objectNcss = ((Integer)vClassMetrics.elementAt(OBJ_NCSS)).intValue();
            int functions  = ((Integer)vClassMetrics.elementAt(OBJ_FCTS)).intValue();
            int classes    = ((Integer)vClassMetrics.elementAt(OBJ_CLSSS)).intValue();
            int jvdcs      = ((Integer)vClassMetrics.elementAt(OBJ_JVDCS)).intValue();
            lObjectSum   += (long)objectNcss;
            lFunctionSum += (long)functions;
            lClassesSum  += (long)classes;
            lJVDCSum     += (long)jvdcs;
            sRetVal += _formatListLine( sClass
					, new int[] { objectNcss
						      , functions
						      , classes
						      , jvdcs     } );
        }

	sRetVal += _formatObjectResume( vObjectMetrics.size()
					, lObjectSum
					, lFunctionSum
					, lClassesSum
					, lJVDCSum            );
        
        return sRetVal;
    }

    private String _formatFunctionResume( int functions
					  , long lFunctionSum
					  , long lCCNSum
					  , long lJVDCSum     )
    {
	StringBuffer sRetVal = new StringBuffer();

        double fAverageNcss = _divide( lFunctionSum, functions );
        double fAverageCCN  = _divide( lCCNSum     , functions );
        double fAverageJVDC = _divide( lJVDCSum    , functions );
        NumberFormat pNumberFormat = new DecimalFormat("#,##0.00");
        sRetVal.append("Average Function NCSS: ").append
               (Util.paddWithSpace(pNumberFormat.format
                                   (fAverageNcss), 10)).
               append(NL);
        sRetVal.append("Average Function CCN:  ").append
               (Util.paddWithSpace(pNumberFormat.format
                                   (fAverageCCN),  10)).
               append(NL);
        sRetVal.append("Average Function JVDC: ").append
               (Util.paddWithSpace(pNumberFormat.format
                                   (fAverageJVDC), 10)).
               append(NL);
        sRetVal.append("Program NCSS:          ").append
               (Util.paddWithSpace(pNumberFormat.format
				   (_javancss.getNcss()), 10)).
	       append(NL);

	return sRetVal.toString();
    }

    public String printFunctionNcss() 
    {
        StringBuffer sRetVal = new StringBuffer(80000);

	Vector vFunctionMetrics = _javancss.getFunctionMetrics();

        sRetVal.append( _formatListHeader( vFunctionMetrics.size()
					   , new String[] { "NCSS"
							    , "CCN"
							    , "JVDC"
							    , "Function" } ) );

        long lFunctionSum = 0;
        long lCCNSum      = 0;
        long lJVDCSum     = 0;
        for( Enumeration eFunctions = vFunctionMetrics.elements()
	     ; eFunctions.hasMoreElements()
	     ; )
        {
            Vector vSingleFunctionMetrics = (Vector)eFunctions.nextElement();
            String sFunction = null;
	    sFunction = (String)vSingleFunctionMetrics.elementAt(FCT_NAME);
            int functionNcss = ((Integer)vSingleFunctionMetrics.elementAt(FCT_NCSS)).intValue();
            int functionCCN  = ((Integer)vSingleFunctionMetrics.elementAt(FCT_CCN )).intValue();
            int functionJVDC = ((Integer)vSingleFunctionMetrics.elementAt(FCT_JVDC)).intValue();
            lFunctionSum += (long)functionNcss;
            lCCNSum      += (long)functionCCN;
            lJVDCSum     += (long)functionJVDC; 
	    sRetVal.append( _formatListLine( sFunction
					     , new int[] { functionNcss
							   , functionCCN
							   , functionJVDC } ) );
        }

	sRetVal.append( _formatFunctionResume( vFunctionMetrics.size()
					       , lFunctionSum
					       , lCCNSum
					       , lJVDCSum              ) );

        return sRetVal.toString();
    }

    public String printJavaNcss()
    {
	return "Java NCSS: " + _javancss.getNcss() + NL;
    }
}
