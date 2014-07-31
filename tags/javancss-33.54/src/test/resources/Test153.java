package issues;

import org.dom4j.Element;
import java.util.Iterator;

public class AnnotationsInForLoops 
{
    public void foo(List relationships)
    {
       for (@SuppressWarnings("unchecked") Iterator itr = relationships.iterator(); itr.hasNext(); )
          ;
    }
   
     public void someXmlProcessing(Element element) {
         for (@SuppressWarnings("unchecked") Iterator<Element> i = element.elementIterator(); i.hasNext();) {
             i.next();
         }
     }

     public void apacheIterator(Iterator some_iterator)
     {
        for (@SuppressWarnings("unchecked") Iterator<SomeType> i = new org.apache.commons.collections.iterators.ListIteratorWrapper(some_iterator); i.hasNext(); ) { 
           
        }
     }
     
     
}
