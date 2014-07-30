/*
Copyright (C) 2014 Chr. Clemens Lee <clemens@kclee.com>.

This file is part of JavaNCSS
(http://javancss.codehaus.org/).

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA*/

package javancss;

import java.io.IOException;
import java.io.Writer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import ccl.util.Util;

/**
 * Generates ascii output of Java metrics.
 *
 * @author    Chr. Clemens Lee <clemens@kclee.com>
 *            , Windows 13 10 line feed feature by John Wilson.
 * @version   $Id$
 */
public class AsciiFormatter implements Formatter
{
    private static final int LEN_NR = 3;
    private static final String NL = System.getProperty( "line.separator" );

    private final Javancss _javancss;

    private String[] _header = null;
    private int      _length = 0;
    private int      _nr     = 0;

    private NumberFormat _pNumberFormat = null;

    private String _formatListHeader( int lines, String[] header )
    {
        _header = header;

        _nr = 0;

        StringBuffer sRetVal = new StringBuffer();

        _length = Util.itoa( lines ).length();
        int spaces = Math.max( 0, _length - LEN_NR );
        _length = spaces + LEN_NR;
        sRetVal.append( Util.multiplyChar( ' ', spaces ) );
        sRetVal.append( "Nr." );
        for( int nr = 0; nr < header.length; nr++ )
        {
            sRetVal.append( ' ' ).append( header[ nr ] );
        }
        sRetVal.append( NL );

        return sRetVal.toString();
    }

    private String _formatListLine( String name, int[] value )
    {
        StringBuffer sLine = new StringBuffer();

        _nr++;
        sLine.append( Util.paddWithSpace( _nr, _length ) );
        for( int index = 0; index < _header.length - 1; index++ )
        {
            sLine.append( ' ' );
            sLine.append( Util.paddWithSpace( value[ index ]
                                              , _header[ index ].length() ) );
        }
        sLine.append( ' ' );
        sLine.append( name );
        sLine.append( NL );

        return sLine.toString();
    }

    private double _divide( int divident, int divisor )
    {
        double dRetVal = 0.0;
        if ( divisor > 0 )
        {
            dRetVal = Math.round( ( (double) divident / (double) divisor ) * 100 ) / 100.0;
        }

        return dRetVal;
    }

    private double _divide( long divident, long divisor )
    {
        double dRetVal = 0.0;
        if ( divisor > 0 )
        {
            dRetVal = Math.round( ( (double) divident / (double) divisor ) * 100 ) / 100.0;
        }

        return dRetVal;
    }

    private String _formatPackageMatrix( int packages
                                         , int classesSum
                                         , int functionsSum
                                         , int javadocsSum
                                         , int ncssSum      )
    {
        ( (DecimalFormat) _pNumberFormat ).applyPattern( "###0.00" );
        int maxItemLength = _pNumberFormat.format( ncssSum ).length();
        maxItemLength = Math.max( 9, maxItemLength );
        String sRetVal =
            Util.paddWithSpace( "Packages" , maxItemLength ) + ' '
            + Util.paddWithSpace( "Classes", maxItemLength ) + ' '
            + Util.paddWithSpace( "Functions", maxItemLength ) + ' '
            + Util.paddWithSpace( "NCSS", maxItemLength ) + ' '
            + Util.paddWithSpace( "Javadocs", maxItemLength )
            + " | per" + NL

            + Util.multiplyChar( '-', ( maxItemLength + 1 ) * 6 + 1 ) + NL
            + Util.paddWithSpace( _pNumberFormat.format( packages ), maxItemLength ) + ' '
            + Util.paddWithSpace( _pNumberFormat.format( classesSum ), maxItemLength ) + ' '
            + Util.paddWithSpace( _pNumberFormat.format( functionsSum ), maxItemLength ) + ' '
            + Util.paddWithSpace( _pNumberFormat.format( ncssSum ), maxItemLength ) + ' '
            + Util.paddWithSpace( _pNumberFormat.format( javadocsSum ), maxItemLength )
            + " | Project" + NL

            + Util.multiplyChar( ' ', maxItemLength + 1 )
            + Util.paddWithSpace( _pNumberFormat.format( _divide( classesSum, packages ) ), maxItemLength ) + ' '
            + Util.paddWithSpace( _pNumberFormat.format( _divide( functionsSum, packages ) ), maxItemLength ) + ' '
            + Util.paddWithSpace( _pNumberFormat.format( _divide( ncssSum, packages ) ), maxItemLength ) + ' '
            + Util.paddWithSpace( _pNumberFormat.format( _divide( javadocsSum, packages ) ), maxItemLength )
            + " | Package" + NL

            + Util.multiplyChar( ' ', (maxItemLength + 1)*2 )
            + Util.paddWithSpace( _pNumberFormat.format( _divide( functionsSum, classesSum ) ), maxItemLength ) + ' '
            + Util.paddWithSpace( _pNumberFormat.format( _divide( ncssSum, classesSum ) ), maxItemLength ) + ' '
            + Util.paddWithSpace( _pNumberFormat.format( _divide( javadocsSum, classesSum ) ), maxItemLength )
            + " | Class" + NL

            + Util.multiplyChar( ' ', (maxItemLength + 1)*3 )
            + Util.paddWithSpace( _pNumberFormat.format( _divide( ncssSum, functionsSum ) ), maxItemLength ) + ' '
            + Util.paddWithSpace( _pNumberFormat.format( _divide( javadocsSum, functionsSum ) ), maxItemLength )
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

    public void printPackageNcss( Writer w )
        throws IOException
    {
        List<PackageMetric> vPackageMetrics = _javancss.getPackageMetrics();

        int packages = vPackageMetrics.size();

        w.write( _formatListHeader( packages
                                            , new String[] {   "  Classes"
                                                             , "Functions"
                                                             , "     NCSS"
                                                             , " Javadocs"
                                                             , "Package" } ) );

        int classesSum   = 0;
        int functionsSum = 0;
        int javadocsSum  = 0;
        int ncssSum      = 0;
        for( PackageMetric pPackageMetric : vPackageMetrics )
        {
            classesSum   += pPackageMetric.classes;
            functionsSum += pPackageMetric.functions;
            ncssSum      += pPackageMetric.ncss;
            javadocsSum  += pPackageMetric.javadocs;
            w.write( _formatListLine( pPackageMetric.name
                                        , new int[] { pPackageMetric.classes
                                                      , pPackageMetric.functions
                                                      , pPackageMetric.ncss
                                                      , pPackageMetric.javadocs
                                        } ) );
        }

        int packagesLength = Util.itoa( packages ).length();
        int spaces = Math.max( packagesLength, LEN_NR ) + 1;
        w.write( Util.multiplyChar( ' ', spaces ) +
               "--------- --------- --------- ---------" + NL );

        w.write( Util.multiplyChar( ' ', spaces )
            + Util.paddWithSpace( classesSum, 9 ) + ' '
            + Util.paddWithSpace( functionsSum, 9 ) + ' '
            + Util.paddWithSpace( ncssSum, 9 ) + ' '
            + Util.paddWithSpace( javadocsSum, 9 )
            + " Total" + NL + NL );

        w.write( _formatPackageMatrix( packages
                                         , classesSum
                                         , functionsSum
                                         , javadocsSum
                                         , ncssSum      ) );
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
        String sRetVal = "Average Object NCSS:             "
            + Util.paddWithSpace( _pNumberFormat.format( fAverageNcss ),     9 ) + NL
            + "Average Object Functions:        "
            + Util.paddWithSpace( _pNumberFormat.format( fAverageFuncs ),    9 ) + NL
            + "Average Object Inner Classes:    "
            + Util.paddWithSpace( _pNumberFormat.format( fAverageClasses ),  9 ) + NL
            + "Average Object Javadoc Comments: "
            + Util.paddWithSpace( _pNumberFormat.format( fAverageJavadocs ), 9 ) + NL
            + "Program NCSS:                    "
            + Util.paddWithSpace( _pNumberFormat.format( _javancss.getNcss() ), 9 ) + NL;

        return sRetVal;
    }

    public void printObjectNcss( Writer w )
        throws IOException
    {
        List<ObjectMetric> vObjectMetrics = _javancss.getObjectMetrics();

        w.write( _formatListHeader( vObjectMetrics.size()
                                            , new String[] { "NCSS"
                                                             , "Functions"
                                                             , "Classes"
                                                             , "Javadocs"
                                                             , "Class"     } ) );
        long lFunctionSum = 0;
        long lClassesSum  = 0;
        long lObjectSum   = 0;
        long lJVDCSum     = 0;
        for ( ObjectMetric classMetric : vObjectMetrics )
        {
            String sClass = classMetric.name;
            int objectNcss = classMetric.ncss;
            int functions  = classMetric.functions;
            int classes    = classMetric.classes;
            int jvdcs      = classMetric.javadocs;
            lObjectSum   += objectNcss;
            lFunctionSum += functions;
            lClassesSum  += classes;
            lJVDCSum     += jvdcs;
            w.write( _formatListLine( sClass
                                        , new int[] { objectNcss
                                                      , functions
                                                      , classes
                                                      , jvdcs     } ) );
        }

        w.write( _formatObjectResume( vObjectMetrics.size()
                                        , lObjectSum
                                        , lFunctionSum
                                        , lClassesSum
                                        , lJVDCSum            ) );
    }

    private String _formatFunctionResume( int functions
                                          , long lFunctionSum
                                          , long lCCNSum
                                          , long lJVDCSum     )
    {
        double fAverageNcss = _divide( lFunctionSum, functions );
        double fAverageCCN  = _divide( lCCNSum     , functions );
        double fAverageJVDC = _divide( lJVDCSum    , functions );

        String sRetVal = "Average Function NCSS: "
            + Util.paddWithSpace( _pNumberFormat.format( fAverageNcss ), 10 ) + NL
            + "Average Function CCN:  "
            + Util.paddWithSpace( _pNumberFormat.format( fAverageCCN ),  10 ) + NL
            + "Average Function JVDC: "
            + Util.paddWithSpace( _pNumberFormat.format( fAverageJVDC ), 10 ) + NL
            + "Program NCSS:          "
            + Util.paddWithSpace( _pNumberFormat.format( _javancss.getNcss() ), 10 ) + NL;

        return sRetVal;
    }

    public void printFunctionNcss( Writer w )
        throws IOException
    {
        List<FunctionMetric> vFunctionMetrics = _javancss.getFunctionMetrics();

        w.write( _formatListHeader( vFunctionMetrics.size()
                                           , new String[] { "NCSS"
                                                            , "CCN"
                                                            , "JVDC"
                                                            , "Function" } ) );

        long lFunctionSum = 0;
        long lCCNSum      = 0;
        long lJVDCSum     = 0;
        for ( FunctionMetric functionMetric : vFunctionMetrics )
        {
            String sFunction = functionMetric.name;
            int functionNcss = functionMetric.ncss;
            int functionCCN  = functionMetric.ccn;
            int functionJVDC = functionMetric.javadocs;

            lFunctionSum += functionNcss;
            lCCNSum      += functionCCN;
            lJVDCSum     += functionJVDC;
            w.write( _formatListLine( sFunction
                                             , new int[] { functionNcss
                                                           , functionCCN
                                                           , functionJVDC } ) );
        }

        w.write( _formatFunctionResume( vFunctionMetrics.size()
                                               , lFunctionSum
                                               , lCCNSum
                                               , lJVDCSum              ) );
    }

    public void printJavaNcss( Writer w )
        throws IOException
    {
        w.write( "Java NCSS: " + _javancss.getNcss() + NL );
    }

    public void printStart( Writer w )
    {
    }

    public void printEnd( Writer w )
    {
    }
}
