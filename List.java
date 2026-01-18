/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;
	
    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }
    
    /** Returns the number of elements in this list. */
    public int getSize() {
 	    return size;
    }

    /** Returns the CharData of the first element in this list. */
    public CharData getFirst() {
        return first.cp;
    }

    /** GIVE Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        CharData newChr = new CharData(chr);
        Node newFirst = new Node (newChr , first);
        first = newFirst; 
        size ++;
    }
    
    /** GIVE Textual representation of this list. */
    public String toString() {
        Node current = first;
        String ans = "(";
        while (current != null ){
            ans += " " + current.cp.toString();
            current = current.next;
        }
        return ans + ")";
    }

    /** Returns the index of the first CharData object in this list
     *  that has the same chr value as the given char,
     *  or -1 if there is no such object in this list. */
    public int indexOf(char chr) {
        int index = 0;
        Node current = first;
        while (current != null){
            if (current.cp.equals(chr)){
                return index;
            }
            index++;
            current = current.next;
        }
        return -1;
    }

    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the beginning of this list. */
    public void update(char chr) {
        if (indexOf(chr) == -1){
            addFirst(chr);
        } else {
            CharData upd = get(indexOf(chr));
            upd.count++;
        }
    }

    /** GIVE If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) {
        if (indexOf(chr) == -1) return false;   // if the given char is not in the list
        Node prev = null;
        Node current = first;
        // pass the list until gets to chr
        while (!current.cp.equals(chr)){
            prev = current;
            current = current.next;
        }
        // if chr is the first in the list
        if (prev == null){
            first = first.next;
            size --;
            return true;
        }
        prev.next = current.next;
        size--;
        return true;
    }

    /** Returns the CharData object at the specified index in this list. 
     *  If the index is negative or is greater than the size of this list, 
     *  throws an IndexOutOfBoundsException. */
    public CharData get(int index) {
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }

        Node current = first;
        int place = 0;

        // pass the list until gets to index
        while (place != index){
            current = current.next;
            place++;
        }
        return current.cp;
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
	    CharData[] arr = new CharData[size];
	    Node current = first;
	    int i = 0;
        while (current != null) {
    	    arr[i++]  = current.cp;
    	    current = current.next;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public ListIterator listIterator(int index) {
	    // If the list is empty, there is nothing to iterate   
	    if (size == 0) return null;
	    // Gets the element in position index of this list
	    Node current = first;
	    int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        // Returns an iterator that starts in that element
	    return new ListIterator(current);
    }

/* 
    public static void main(String[] args) {
        System.out.println("--- Starting Final List Test ---");

        // 1. יצירת רשימה והוספת איברים
        List list = new List();
        list.addFirst('i');
        list.addFirst('n');
        list.addFirst('u');
        list.addFirst('R');
        
        System.out.println("List created: " + list.toString()); // אמור להדפיס Runi

        // 2. בדיקת האיטרטור (ListIterator)
        // זה החלק החדש שמשתמש במחלקה ששלחת הרגע
        System.out.println("\n--- Testing ListIterator ---");
        
        // נתחיל מאינדקס 0
        ListIterator it = list.listIterator(0);
        
        System.out.println("Iterating over the list:");
        while (it.hasNext()) {
            // it.next() מחזיר אובייקט CharData, אנחנו מדפיסים אותו
            System.out.println(it.next()); 
        }

        // 3. בדיקת איטרטור מאמצע הרשימה
        System.out.println("\n--- Testing ListIterator from index 2 ---");
        ListIterator it2 = list.listIterator(2); // אמור להתחיל מ-'n'
        while (it2.hasNext()) {
            System.out.println(it2.next());
        }

        // 4. בדיקת מחיקה (חשוב: וודא שתיקנת את הבאג ב-List.java לפני הרצה זו)
        System.out.println("\n--- Testing remove('u') ---");
        boolean removed = list.remove('u');
        System.out.println("Removed? " + removed);
        System.out.println("List after remove: " + list.toString()); // אמור להיות Rni

        // 5. בדיקת חריגה (Exception)
        System.out.println("\n--- Testing Exception ---");
        try {
            list.get(10); // אינדקס לא חוקי
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Success: Exception caught!");
        }

        // המחרוזת מהדוגמה
        String word = "committee_"; 
        
        List list1 = new List();
        
        // מעבר על המחרוזת ובניית הרשימה
        for (int i = 0; i < word.length(); i++) {
            list1.update(word.charAt(i));
        }
        
        System.out.println("Result: " + list1.toString());
        // צפי לפי הדף (סדר התווים עשוי להיות הפוך תלוי במימוש addFirst, אבל הספירה חייבת להיות זהה):
        // c:1, o:1, m:2, i:1, t:2, e:2, _:1
    } */


    
} 