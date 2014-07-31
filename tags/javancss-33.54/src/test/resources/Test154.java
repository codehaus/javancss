package issues;

import java.math.BigDecimal;

public class ExtraBraces 
{

   public boolean isIntegerFine(final Class<?> type) {
      if (Integer.class.equals(type) || int.class.equals(type)) {
          return true;
      }
      return false;
  }
   
   public boolean isIntegerBroken(final Class<?> type) {
      if (Integer.class.equals(type) || (int.class.equals(type))) {
          return true;
      }
      return false;
  }
    
}
