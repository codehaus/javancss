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
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Vector;

/**
 * Generates XML output of Java metrics.
 *
 * @author    Chr. Clemens Lee <clemens@kclee.com>
 * @version   $Id: XmlFormatter.java,v 1.6 2006/04/16 11:42:25 clemens Exp clemens $
 */
public class XmlFormatter implements Formatter
				     , JavancssConstants
{
    private Javancss _javancss = null;

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

    static NumberFormat _pNumberFormat = null;

    private String _formatPackageMatrix( int packages
					 , int classesSum
					 , int functionsSum
					 , int ncssSum
					 , int javadocsSum				// added by SMS
					 , int javadocLnSum				// added by SMS
					 , int singleLnSum				// added by SMS
					 , int multiLnSum		)		// added by SMS
    {
        //NumberFormat pNumberFormat = new DecimalFormat("#,##0.00");

        String sRetVal =
	    "    <table>\n"
	    + "      <tr><td>Packages</td><td>Classes</td><td>Functions</td><td>NCSS</td><td>Javadocs</td><td>per</td></tr>\n"


            + "      <tr><td>" 
	    + _pNumberFormat.format( packages ) 
	    + "</td><td>"
	    + _pNumberFormat.format( classesSum )
	    + "</td><td>"
	    + _pNumberFormat.format( functionsSum )
	    + "</td><td>"
	    + _pNumberFormat.format( ncssSum )
	    + "</td><td>"
	    + _pNumberFormat.format( javadocsSum )
	    + "</td><td>Project</td></tr>\n"
            + "      <tr><td></td><td>"
            + _pNumberFormat.format( _divide( classesSum, packages ) )
	    + "</td><td>"
	    + _pNumberFormat.format( _divide( functionsSum, packages ) )
	    + "</td><td>"
	    + _pNumberFormat.format( _divide( ncssSum, packages ) )
	    + "</td><td>"
	    + _pNumberFormat.format( _divide( javadocsSum, packages ) )
	    + "</td><td>Package</td></tr>\n"
            + "      <tr><td></td><td></td><td>"
	    + _pNumberFormat.format( _divide( functionsSum, classesSum ) )
	    + "</td><td>"
	    + _pNumberFormat.format( _divide( ncssSum, classesSum ) )
	    + "</td><td>"
	    + _pNumberFormat.format( _divide( javadocsSum, classesSum ) )
	    + "</td><td>Class</td></tr>\n"
            + "      <tr><td></td><td></td><td></td><td>"
	    + _pNumberFormat.format( _divide( ncssSum, functionsSum ) )
            + "</td><td>"
               //+ _pNumberFormat.format( _divide( ncssSum, functionsSum ) )
            + _pNumberFormat.format( _divide( javadocsSum, functionsSum ) )
	    + "</td><td>Function</td></tr>\n"
	    + "    </table>\n";
        /*
        String sRetVal =
	    "    <table>\n"
	    + "      <tr><td>Packages</td><td>Classes</td><td>Functions</td><td>NCSS</td>"
            + "<td>javadocs</td><td>javadocs_lines</td><td>single_comment_lines"
            + "</td><td>implementation_comment_lines</td><td>per</td></tr>\n"
            + "      <tr><td>" 
	    + _pNumberFormat.format( packages ) 
	    + "</td><td>"
	    + _pNumberFormat.format( classesSum )
	    + "</td><td>"
	    + _pNumberFormat.format( functionsSum )
	    + "</td><td>"
	    + _pNumberFormat.format( ncssSum )
		+ "</td><td>"
	    + _pNumberFormat.format( javadocSum )
		+ "</td><td>"
	    + _pNumberFormat.format( javadocLnSum )
		+ "</td><td>"
	    + _pNumberFormat.format( singleLnSum )
		+ "</td><td>"
	    + _pNumberFormat.format( multiLnSum )
	    + "</td><td>Project</td></tr>\n"
            + "      <tr><td></td><td>"
            + _pNumberFormat.format( _divide( classesSum, packages ) )
	    + "</td><td>"
	    + _pNumberFormat.format( _divide( functionsSum, packages ) )
	    + "</td><td>"
	    + _pNumberFormat.format( _divide( ncssSum, packages ) )
		+ "</td><td>"
	    + _pNumberFormat.format( _divide( javadocSum, packages ) )
		+ "</td><td>"
	    + _pNumberFormat.format( _divide( javadocLnSum, packages ) )
		+ "</td><td>"
	    + _pNumberFormat.format( _divide( javadocLnSum, packages ) )
		+ "</td><td>"
	    + _pNumberFormat.format( _divide( multiLnSum, packages ) )
	    + "</td><td>Package</td></tr>\n"
            + "      <tr><td></td><td></td><td>"
	    + _pNumberFormat.format( _divide( functionsSum, classesSum ) )
	    + "</td><td>"
	    + _pNumberFormat.format( _divide( ncssSum, classesSum ) )
		+ "</td><td>"
	    + _pNumberFormat.format( _divide( javadocSum, classesSum ) )
		+ "</td><td>"
	    + _pNumberFormat.format( _divide( javadocLnSum, classesSum ) )
		+ "</td><td>"
	    + _pNumberFormat.format( _divide( javadocLnSum, classesSum ) )
		+ "</td><td>"
	    + _pNumberFormat.format( _divide( multiLnSum, classesSum ) )
	    + "</td><td>Class</td></tr>\n"
            + "      <tr><td></td><td></td><td></td><td>"
	    + _pNumberFormat.format( _divide( ncssSum, functionsSum ) )
		+ "</td><td>"
	    + _pNumberFormat.format( _divide( javadocSum, functionsSum ) )
		+ "</td><td>"
	    + _pNumberFormat.format( _divide( javadocLnSum, functionsSum ) )
		+ "</td><td>"
	    + _pNumberFormat.format( _divide( javadocLnSum, functionsSum ) )
		+ "</td><td>"
	    + _pNumberFormat.format( _divide( multiLnSum, functionsSum ) )
	    + "</td><td>Function</td></tr>\n"
	    + "    </table>\n";
        */

	return sRetVal;
    }

    public XmlFormatter( Javancss javancss )
    {
	super();

	_javancss = javancss;

        _pNumberFormat = NumberFormat.getInstance( Locale.US );
        ((DecimalFormat)_pNumberFormat).applyPattern( "#,##0.00" );
    }

    public String printPackageNcss() 
    {
	String sRetVal = "  <packages>\n";
	Vector vPackageMetrics = _javancss.getPackageMetrics();

        int packages = vPackageMetrics.size();
        int classesSum = 0;
        int functionsSum = 0;
        int ncssSum = 0;
		//added by SMS
        int javadocSum = 0;
        int javadocLnSum = 0;
        int singleLnSum = 0;
        int multiLnSum = 0;
        //
        for( Enumeration ePackages = vPackageMetrics.elements()
	     ; ePackages.hasMoreElements()
	     ; )
        {
            PackageMetric pPackageMetric = (PackageMetric)ePackages.nextElement();

            classesSum += pPackageMetric.classes;
            functionsSum += pPackageMetric.functions;
            ncssSum += pPackageMetric.ncss;
			// added by SMS
            javadocSum += pPackageMetric.javadocs;
            javadocLnSum += pPackageMetric.javadocsLn;
            singleLnSum += pPackageMetric.singleLn;
            multiLnSum += pPackageMetric.multiLn;
            //
	    sRetVal += 
                   "    <package>\n" +
                   "      <name>" + pPackageMetric.name + "</name>\n" + 
                   "      <classes>" + pPackageMetric.classes + "</classes>\n" +
                   "      <functions>" + pPackageMetric.functions + "</functions>\n" +
                   "      <ncss>" + pPackageMetric.ncss + "</ncss>\n" +
                   "      <javadocs>" + pPackageMetric.javadocs + "</javadocs>\n" +
                   "      <javadoc_lines>" + pPackageMetric.javadocsLn + "</javadoc_lines>\n" +
                   "      <single_comment_lines>" + pPackageMetric.singleLn + "</single_comment_lines>\n" + 
                   "      <multi_comment_lines>" + pPackageMetric.multiLn + "</multi_comment_lines>\n" +
                   "    </package>\n";
        }

	sRetVal +=
               "    <total>\n" +
               "      <classes>" + classesSum + "</classes>\n" +
               "      <functions>" + functionsSum + "</functions>\n" +
               "      <ncss>" + ncssSum + "</ncss>\n" +
               "      <javadocs>" + javadocSum + "</javadocs>\n" +
               "      <javadoc_lines>" + javadocLnSum + "</javadoc_lines>\n" +
               "      <single_comment_lines>" + singleLnSum + "</single_comment_lines>\n" + 
               "      <multi_comment_lines>" + multiLnSum + "</multi_comment_lines>\n" +
               "    </total>\n";

	sRetVal += _formatPackageMatrix( packages
					 , classesSum
					 , functionsSum
					 , ncssSum 
   					 , javadocSum				// added by SMS
					 , javadocLnSum				// added by SMS
					 , singleLnSum				// added by SMS
					 , multiLnSum		);		// added by SMS

	sRetVal += "  </packages>\n";

        return sRetVal;
    }

    private String _formatObjectResume( int objects
					, long lObjectSum
					, long lFunctionSum
					, long lClassesSum
					, long lJVDCSum
					, long lJVDCLSum
					, long lSLSum
					, long lMLSum		)
    {
        double fAverageNcss     = _divide( lObjectSum  , objects );
        double fAverageFuncs    = _divide( lFunctionSum, objects );
        double fAverageClasses  = _divide( lClassesSum , objects );
        double fAverageJavadocs = _divide( lJVDCSum    , objects );
		// added by SMS
		double fAverageJVDCL = _divide( lJVDCLSum   , objects );
		double fAverageSL	 = _divide( lSLSum		, objects );
		double fAverageML	 = _divide( lMLSum		, objects );
		//
        //NumberFormat _pNumberFormat = new DecimalFormat("#,##0.00");
        String sRetVal = 
	    "    <averages>\n" +
	    "      <ncss>" + _pNumberFormat.format( fAverageNcss ) + "</ncss>\n" +
	    "      <functions>" + _pNumberFormat.format( fAverageFuncs ) + "</functions>\n" +
	    "      <classes>" + _pNumberFormat.format( fAverageClasses ) + "</classes>\n" +
	    "      <javadocs>" + _pNumberFormat.format( fAverageJavadocs ) + "</javadocs>\n" +
               //		"      <javadocs_lines>" + _pNumberFormat.format( fAverageJVDCL ) + "</javadocs_lines>\n" +
               //		"      <single_comment_lines>" + _pNumberFormat.format( fAverageSL ) + "</single_comment_lines>\n" + 
               //		"      <implementation_comment_lines>" + _pNumberFormat.format( fAverageML ) + "</implementation_comment_lines>\n" +
	    "    </averages>\n" +
	    "    <ncss>" + _pNumberFormat.format( _javancss.getNcss() ) + "</ncss>\n";
	
	return sRetVal;
    }

    public String printObjectNcss() {
	String sRetVal = "  <objects>\n";

	Vector vObjectMetrics = _javancss.getObjectMetrics();

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

            // added by SMS
            int jvdcsl     = ((Integer)vClassMetrics.elementAt(OBJ_JVDC_LINES)).intValue();
            int singlel	   = ((Integer)vClassMetrics.elementAt(OBJ_SINGLE_LINES)).intValue();
            int multil     = ((Integer)vClassMetrics.elementAt(OBJ_MULTI_LINES)).intValue();
            //
            lObjectSum   += (long)objectNcss;
            lFunctionSum += (long)functions;
            lClassesSum  += (long)classes;
            lJVDCSum     += (long)jvdcs;
            sRetVal += 
		"    <object>\n" +
		"      <name>"      + sClass     + "</name>\n"      +
		"      <ncss>"      + objectNcss + "</ncss>\n"      +
		"      <functions>" + functions  + "</functions>\n" +
		"      <classes>"   + classes    + "</classes>\n"   +
		"      <javadocs>"  + jvdcs      + "</javadocs>\n"  +
                   //		"      <javadocs_lines>" + jvdcsl + "</javadocs_lines>\n" +
                   //		"      <single_comment_lines>" + singlel + "</single_comment_lines>\n" + 
                   //		"      <implementation_comment_lines>" + multil + "</implementation_comment_lines>\n" +
		"    </object>\n";
        }

	sRetVal += _formatObjectResume( vObjectMetrics.size()
					, lObjectSum
					, lFunctionSum
					, lClassesSum
					, lJVDCSum
					, _javancss.getJdcl()
                                        , _javancss.getSl()
					, _javancss.getMl()
                                        );

	sRetVal += "  </objects>\n";
        
        return sRetVal;
    }

    private String _formatFunctionResume( int functions
					  , long lFunctionSum
					  , long lCCNSum
					  , long lJVDCSum     
					  , long lJVDCLSum
					  , long lSLSum
					  , long lMLSum		)
    {
	StringBuffer sRetVal = new StringBuffer();

        double fAverageNcss = _divide( lFunctionSum, functions );
        double fAverageCCN  = _divide( lCCNSum     , functions );
        double fAverageJVDC = _divide( lJVDCSum    , functions );

		// added by SMS
		double fAverageJVDCL = _divide( lJVDCLSum   , functions );
		double fAverageSL	 = _divide( lSLSum		, functions );
		double fAverageML	 = _divide( lMLSum		, functions );
		//

        //NumberFormat _pNumberFormat = new DecimalFormat("#,##0.00");
        sRetVal.append("    <function_averages>\n" +
		       "      <ncss>" + _pNumberFormat.format( fAverageNcss ) + "</ncss>\n" + 
		       "      <ccn>"  + _pNumberFormat.format( fAverageCCN  ) + "</ccn>\n"  + 
 		       "      <javadocs>" + _pNumberFormat.format( fAverageJVDC ) + "</javadocs>\n" +
                       //			   "      <javadocs_lines>" + _pNumberFormat.format( fAverageJVDCL ) + "</javadocs_lines>\n" +
                       //			   "      <single_comment_lines>" + _pNumberFormat.format( fAverageSL ) + "</single_comment_lines>\n" + 
                       //			   "      <implementation_comment_lines>" + _pNumberFormat.format( fAverageML ) + "</implementation_comment_lines>\n" +
 		       "    </function_averages>\n" +
		       "    <ncss>" + _pNumberFormat.format( _javancss.getNcss() ) + "</ncss>\n" );

	return sRetVal.toString();
    }

    public String printFunctionNcss() 
    {
        StringBuffer sRetVal = new StringBuffer(80000);

	sRetVal.append( "  <functions>\n" );

	Vector vFunctionMetrics = _javancss.getFunctionMetrics();

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

			// added by SMS
			int functionJVDCL	= ((Integer)vSingleFunctionMetrics.elementAt(FCT_JVDC_LINES)).intValue();
			int functionSL		= ((Integer)vSingleFunctionMetrics.elementAt(FCT_SINGLE_LINES)).intValue();
			int functionML		= ((Integer)vSingleFunctionMetrics.elementAt(FCT_MULTI_LINES)).intValue();
			//

            lFunctionSum += (long)functionNcss;
            lCCNSum      += (long)functionCCN;
            lJVDCSum     += (long)functionJVDC; 
	    sRetVal.append( 
			   "    <function>\n" +
			   "      <name>" + sFunction + "</name>\n" +
			   "      <ncss>" + functionNcss + "</ncss>\n" +
			   "      <ccn>"  + functionCCN  + "</ccn>\n"  +
			   "      <javadocs>" + functionJVDC + "</javadocs>\n" +
                           //			   "      <javadocs_lines>" + functionJVDCL + "</javadocs_lines>\n" +
                           //			   "      <single_comment_lines>" + functionSL + "</single_comment_lines>\n" + 
                           //			   "      <implementation_comment_lines>" + functionML + "</implementation_comment_lines>\n" +
			   "    </function>\n" );
        }

	sRetVal.append( _formatFunctionResume( vFunctionMetrics.size()
					       , lFunctionSum
					       , lCCNSum
					       , lJVDCSum              
                                               , _javancss.getJdcl()
                                               , _javancss.getSl()
                                               , _javancss.getMl()
                                               ) );

	sRetVal.append( "  </functions>\n" );

        return sRetVal.toString();
    }

    public String printJavaNcss() {
	return	"  <ncss>" + _javancss.getNcss() + "</ncss>\n" 
               //+
               //"  <javadocs>" + _javancss.getJvdc() + "</javadocs>\n" +
               //"  <javadocs_lines>" + _javancss.getJdcl() + "</javadocs_lines>\n" +
               //"  <single_comment_lines>" + _javancss.getSl() + "</single_comment_lines>\n" + 
               //"  <implementation_comment_lines>" + _javancss.getSl() + "</implementation_comment_lines>\n";
               ;
    }

    static public String printStart()
    {
        Calendar calendar = Util.getCalendar();

        return "<?xml version=\"1.0\"?>\n" +
               "<javancss>\n" +
               "  <date>" + Util.getDate( calendar ) + "</date>\n" +
               "  <time>" + Util.getTime( calendar ) + "</time>\n";
    }

    static public String printEnd()
    {
        return "</javancss>\n";
    }
}
