import java.util.*;

public class GroupIterator {
    public static void main(String[] args) {
      SuperIterator<String> it = new SuperIterator<>(Arrays.asList(Arrays.asList("a", "b", "c").iterator(), 
                                                                   Arrays.asList("c", "d", "e", "f").iterator()));
      while(it.hasNext()){
          System.out.println(it.next());
      }
    }
    
    public static class SuperIterator<T> implements Iterator<T> {
        private final Iterator<Iterator<T>> iterators;
        private Iterator<T> current;
        
        SuperIterator(List<Iterator<T>> iterators){
            this.iterators = iterators.iterator();
        }
        
        @Override
        public boolean hasNext(){
            if(current!=null && current.hasNext()){
                return true;
            } else if(iterators.hasNext()){
                current = iterators.next();
                return hasNext();
            } else {
                return false;
            }
        }
        
        @Override
        public T next(){
            return current.next();
        }
    }
}
