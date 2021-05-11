/*
 * Program umozliwia operacje na pociagach, ktore przyjechaly do stacji.
 * Kazdy z pociagow ma swoja nazwe i liste wagonow, zawierajaca co najmniej jeden wagon.
 * Nazwy pociagow nie moga sie powtarzac.
 * Jezeli jest to konieczne, program wypisuje, ze dany pociag jest juz na stacji lub ze dany pociag nie istnieje.
 */
import java.util.Scanner;

class Source {
    public static Scanner scan = new Scanner(System.in); // zmienna klasy Scanner dla wejscia

    public static void main(String[] args) {
        int numbOfSets = scan.nextInt(); // zmienna przechowuje ilosc zestawow danych
        TrainsList lista = new TrainsList(); // tworzymy liste glowna

        for (int i = 1; i <= numbOfSets; i++) { // osobno dla kazdego zestawu danych
            int numbOfOrders = scan.nextInt(); // liczba polecen

            for (int j=1; j <= numbOfOrders; j++) { // osobno dla kazdego polecenia
                String order = scan.next(); // pobieramy polecenie
                // parametry dla danych polecen - mozliwe 0, 1 lub 2
                String firstParam = "";
                String secondParam = "";

                // dzialanie kazdej z ponizszych funkcji opisane pozniej
                if (order.equals("New")) {
                    firstParam = scan.next();
                    secondParam = scan.next();
                    lista.newTrain(firstParam, secondParam);
                    continue;
                }

                if (order.equals("Display")) {
                    firstParam = scan.next();
                    lista.display(firstParam);
                    continue;
                }

                if (order.equals("Trains")) {
                    lista.wTrains();
                    continue;
                }

                if (order.equals("InsertFirst")) {
                    firstParam = scan.next();
                    secondParam = scan.next();
                    lista.insertFirst(firstParam, secondParam);
                    continue;
                }

                if (order.equals("InsertLast")) {
                    firstParam = scan.next();
                    secondParam = scan.next();
                    lista.insertLast(firstParam, secondParam);
                    continue;
                }

                if (order.equals("DelFirst")) {
                    firstParam = scan.next();
                    secondParam = scan.next();
                    lista.delFirst(firstParam, secondParam);
                    continue;
                }

                if (order.equals("DelLast")) {
                    firstParam = scan.next();
                    secondParam = scan.next();
                    lista.delLast(firstParam, secondParam);
                    continue;
                }

                if (order.equals("Union")) {
                    firstParam = scan.next();
                    secondParam = scan.next();
                    lista.union(firstParam, secondParam);
                    continue;
                }

                if (order.equals("Reverse")) {
                    firstParam = scan.next();
                    lista.reverse(firstParam);
                    continue;
                }
            }
            lista.trains = null; // resetujemy referencje do listy pociagow
        }
    }
}

/*
 * jednostronna lista dwukierunkowa, cykliczna, ktorej referencja first wskazuje pierwszy
 * element listy dla reprezentacji listy wagonow danego pociagu
 */
class Train {
    public String trainName; // nazwa pociagu
    public Train next; // referencja do nastepnego elementu
    public Carriage first;

    public Train(String name) { // konstruktor
        this.trainName = name;
    }
}

// lista wagonow
class Carriage {
    public String carrName; // nazwa wagonu
    public Carriage prev; // pamieta poprzedni wagon
    public Carriage next; // pamieta nastepny wagon

    Carriage(String name) { // konstruktor
        carrName = name;
    }
}

/*
 * lista pojedyncza, ktorej refencja trains wskazuje pierwszy element listy dla reprezentacji
 * listy pociagow
 */
class TrainsList {
    Train trains;

    public TrainsList() { // konstruktor listy pustej
        trains = null;
    }

    /*
     * tworzy nowy pociag o nazwie zadanej pierwszym argumentem z jednym wagonem o nazwie zadanej drugim argumentem
     * i wstawia go do listy pociagow
     */
    public void newTrain(String tName, String cName) {
        if (locate(tName)==null) { // jezeli nie znajdziemy pociagu o tej nazwie na liscie
                Train t = new Train(tName); // tworzymy nowy pociag
                // wstawiamy pociag na poczatek listy, a wczesniejszy pierwszy bedzie drugim w kolejnosci
                t.next = trains;
                trains = t;
                t.first = new Carriage(cName); // tworzymy wagon dla nowego pociagu
                // ustawiamy jego next i prev na niego samego (lista cykliczna)
                t.first.prev = t.first;
                t.first.next = t.first;

        } else { // znalezlismy pociag o tej nazwie na liscie
            System.out.println("Train " + tName + " already exists");
        }
    }

    /*
    * wstawia na poczatek pociagu o nazwie zadanej pierwszym argumentem
    * wagon o nazwie zadanej drugim argumentem
    */
    public void insertFirst (String tName, String cName) {
        Train curr = locate(tName); // lokalizujemy na liscie pociag

        if (curr != null) { // jezeli na liscie istnieje pociag o danej nazwie
            Carriage c = new Carriage(cName); // tworzymy nowy wagon
            /* ustawiamy wartosci next i prev nowego wagonu
             * next wskazuje wczesniejszy pierwszy wagon
             * prev wskazuje ostatni wagon
             * next ostatniego wagonu i prev poprzednio pierwszego ustawiamy na nowy wagon
             */
            c.next = curr.first;
            c.prev = curr.first.prev;
            curr.first.prev.next = c;
            curr.first.prev = c;
            curr.first = c; // dodajemy na poczatek, wiec zmiana referencji first

        } else { // na liscie nie istnieje taki pociag
            System.out.println("Train " + tName + " does not exist");
        }
    }

    /*
     * wstawia na koniec pociagu o nazwie zadanej pierwszym argumentem
     * wagon o nazwie zadanej drugim argumentem
     */
    public void insertLast (String tName, String cName) {
        Train curr = locate(tName); // lokalizujemy na liscie pociag

        if (curr != null) { // jezeli na liscie istnieje pociag o danej nazwie
            Carriage c = new Carriage(cName); // tworzymy nowy wagon
            /* ustawiamy wartosci next i prev nowego wagonu
             * next wskazuje pierwszy wagon
             * prev wskazuje wczesniejszy ostatni wagon
             * next poprzednio ostatniego wagonu i prev pierwszego ustawiamy na nowy wagon
             */
            c.next = curr.first;
            c.prev = curr.first.prev;
            curr.first.prev.next = c;
            curr.first.prev = c;

        } else { // na liscie nie istnieje taki pociag
            System.out.println("Train " + tName + " does not exist");
        }
    }

    /*
     * odwraca kolejnosc wagonow w pociagu o nazwie zadanej argumentem
     * zmienia referencje next i prev pierwszego i ostatniego wagonu, aby zaznaczyc, ze jest odwrocony
     * "naprawa", tzn. ulozenie wszystkich wagonow w odpowiedniej kolejnosci ma miejsce w display
     */
    public void reverse (String tName) {
        Train curr = locate(tName); // lokalizujemy na liscie pociag

        if (curr != null) { // jezeli na liscie istnieje pociag o danej nazwie
            Carriage tmp = curr.first.next; // pamieta wagon kolejny po pierwszym
            // zamiana referencji next pierwszego wagonu na prev i na odwrot
            curr.first.next = curr.first.prev;
            curr.first.prev = tmp;

            // pierwszy wagon zmieniony, wiec teraz odwolujemy sie do niego przez curr.first.next
            tmp = curr.first.next.next; // pamieta ostatni wagon

            // zamiana referencji next ostatniego wagonu na prev i na odwrot
            curr.first.next.next = curr.first.next.prev;
            curr.first.next.prev = tmp;
            // pierwszym pociagiem teraz bedzie ostatni
            curr.first = curr.first.next;

        } else { // na liscie nie istnieje taki pociag
            System.out.println("Train " + tName + " does not exist");
        }
    }

    /*
     * wypisuje liste wagonow pociagu o nazwie zadanej argumentem
     * od pierwszego do ostatniego
     */
    public void display (String T) {
        String dis = ""; // zbieramy wagony do pomocniczego stringa (outputowego)
        Train curr = locate(T); // lokalizujemy na liscie pociag

        if (curr != null) { // jezeli na liscie istnieje pociag o danej nazwie
            Carriage tmp = curr.first; // pamieta pierwszy wagonik
            do {
                // gdy pociag odwrocony, naprawiamy go przez zmiane referencji dla wczesniej nieprzestawionych wagonow
                if (tmp.next.prev != tmp) {
                    Carriage c = tmp.next.next; // zapamietujemy obecnie rozwazany wagon
                    // zmieniamy next i prev wagonu po nim (zgodnie z kolejnoscia odwroconego pociagu)
                    tmp.next.next = tmp.next.prev;
                    tmp.next.prev = c;
                }
                dis += " " + tmp.carrName; // dodajemy nazwe wagonu na wyjscie
                tmp = tmp.next; // przechodzimy do nastepnego wagonu
            } while (tmp != curr.first);

            System.out.println(T + ":" + dis); // wypisujemy liste wagonow pociagu

        } else { // na liscie nie istnieje taki pociag
            System.out.println("Train " + T + " does not exist");
        }
    }

    // wypisuje aktualna liste pociagow od poczatku do konca
    public void wTrains() {
        String dis = "Trains:"; // pomocniczy string na wyjscie
        Train curr = trains; // wskazuje na pierwszy pociag na liscie

        while (curr != null) { // dodajemy nazwy kolejnych pociagow do dis az do ostatniego
            dis += " " + curr.trainName;
            curr = curr.next; // przechodzimy do nastepnego pociagu
        }

        System.out.println(dis); // wypisujemy string outputowy
    }

    /*
     * dolacza pociag o nazwie zadanej drugim argumentem
     * do pociagu o nazwie zadanej pierwszym argumentem
     * na koniec usuwa pociag o nazwie zadanej drugim argumentem
     */
    public void union (String tFirst, String tSecond) {
        // wszystkie zmienne zapamietuja referencje do pierwszego pociagu na liscie
        Train i = trains; // iterator dla petli
        Train curr = trains; // zapamieta referencje do pociagu tFirst (o ile istnieje)
        Train sec = trains; // zapamieta referencje do pociagu tSecond (o ile istnieje)

         /*
          * przeszukanie listy pociagow do momentu, gdy nie znajdziemy obu szukanych pociagow
          * lub dojdziemy do konca listy
          */
        while (i != null) {
            if (!sec.trainName.equals(tSecond)) {
                sec = sec.next;
            }
            if (!curr.trainName.equals(tFirst)) {
                curr = curr.next;
            }
            if (curr!=null && sec!=null) {
                if (curr.trainName.equals(tFirst)) {
                    if (sec.trainName.equals(tSecond)) {
                        break;
                    }
                }
            }
            i = i.next;
        }

        if (curr == null) { // jezeli pierwszy podany pociag nie istnieje na liscie
            System.out.println("Train " + tFirst + " does not exist");
            return;
        }

        if (sec == null) { // jezeli drugi podany pociag nie istnieje na liscie
            System.out.println("Train " + tSecond + " does not exists");
            return;
        }

        Carriage tmp = curr.first.prev; // zapamietuje referencje do ostatniego wagonu pociagu 1
        curr.first.prev.next = sec.first; // ustawia next ostatniego wagonu pociagu 1 na pierwszy wagon pociagu 2
        sec.first.prev.next = curr.first; // ustawia next ostatniego wagonu pociagu 2 na pierwszy wagon pociagu 1
        curr.first.prev = sec.first.prev; // ustawia prev pierwszego wagonu pociagu 1 na ostatni wagon pociagu 2
        sec.first.prev = tmp; // ustawia prev pierwszego wagonu pociagu 2 na ostatni wagon pociagu 1

        delTrain(tSecond); // usuwa pociag 2
    }

    /*
     * usuwa pierwszy wagon z pociagu o nazwie zadanej pierwszym argumentem
     * tworzy nowy pociag o nazwie zadanej drugim argumentem i dodaje mu ten wagon
     * jesli byl to jedyny wagon pociagu 1, to usuwa ten pociag
     */
    public void delFirst (String tFirst, String tSecond) {
        // wszystkie zmienne zapamietuja referencje do pierwszego pociagu na liscie
        Train i = trains; // iterator dla petli
        Train curr = trains; // zapamieta referencje do pociagu tFirst (o ile istnieje)
        Train sec = trains; // zapamieta referencje do pociagu tSecond (o ile istnieje)

        /*
         * przeszukanie listy pociagow do momentu, gdy nie znajdziemy obu szukanych pociagow
         * lub dojdziemy do konca listy
         */
        while (i != null) {
            if (!sec.trainName.equals(tSecond)) {
                sec = sec.next;
            }
            if (!curr.trainName.equals(tFirst)) {
                curr = curr.next;
            }
            if (curr!=null && sec!=null) {
                if (curr.trainName.equals(tFirst)) {
                    if (sec.trainName.equals(tSecond)) {
                        break;
                    }
                }
            }
            i = i.next;
        }

        if (curr == null) { // jezeli pierwszy podany pociag nie istnieje na liscie
            System.out.println("Train " + tFirst + " does not exist");
            return;
        }

        if (sec != null) { // jezeli pierwszy podany pociag istnieje na liscie, nie mozemy go stworzyc
            System.out.println("Train " + tSecond + " already exists");
            return;
        }

        newTrain(tSecond, curr.first.carrName); // tworzy pociag 2 z pierwszym wagonem pociagu 1
        if (curr.first == curr.first.next) { // gdy byl to jedyny wagon pociagu 1, usuwa pociag
            delTrain(tFirst);

        } else {
            if (curr.first.next.next == curr.first) { // pociag odwrocony
                /*
                 * kolejnosc wagonow dla odwroconego pociagu nie zostala naprawiona przez display
                 * zatem biezemy wagon, ktory zgodnie z kolejnoscia odworoconego pociagu powinnien byc
                 * drugi i przypisujemy mu odpowiednie referencje
                 */
                curr.first.next.next = curr.first.next.prev; // next drugiego to trzeci
                curr.first.next.prev = curr.first.prev; // prev drugiego to ostatni
                curr.first.prev.next = curr.first.next; // next ostatniego to drugi
                curr.first = curr.first.next; // drugi bedzie teraz pierwszym
            } else { // pociag nie zostal odwrocony lub nastapilo display, ktore go naprawilo
                curr.first.next.prev = curr.first.prev; // prev drugiego wagonu wskaze ostatni
                curr.first.prev.next = curr.first.next; // next ostatniego wagonu wskaze drugi
                curr.first = curr.first.next; // drugi wagon bedzie teraz pierwszym
            }
        }
    }

    /*
     * usuwa ostatni wagon z pociagu o nazwie zadanej pierwszym argumentem
     * tworzy nowy pociag o nazwie zadanej drugim argumentem i dodaje mu ten wagon
     * jesli byl to jedyny wagon pociagu 1, to usuwa ten pociag
     */
    public void delLast (String tFirst, String tSecond) {
        // wszystkie zmienne zapamietuja referencje do pierwszego pociagu na liscie
        Train i = trains; // iterator dla petli
        Train curr = trains; // zapamieta referencje do pociagu tFirst (o ile istnieje)
        Train sec = trains; // zapamieta referencje do pociagu tSecond (o ile istnieje)

        /*
         * przeszukanie listy pociagow do momentu, gdy nie znajdziemy obu szukanych pociagow
         * lub dojdziemy do konca listy
         */
        while (i != null) {
            if (!sec.trainName.equals(tSecond)) {
                sec = sec.next;
            }
            if (!curr.trainName.equals(tFirst)) {
                curr = curr.next;
            }
            if (curr!=null && sec!=null) {
                if (curr.trainName.equals(tFirst)) { // poprawic
                    if (sec.trainName.equals(tSecond)) {
                        break;
                    }
                }
            }
            i = i.next;
        }

        if (curr == null) { // jezeli pierwszy podany pociag nie istnieje na liscie
            System.out.println("Train " + tFirst + " does not exist");
            return;
        }

        if (sec != null) { // jezeli pierwszy podany pociag istnieje na liscie, nie mozemy go stworzyc
            System.out.println("Train " + tSecond + " already exists");
            return;
        }

        newTrain(tSecond, curr.first.prev.carrName); // tworzy pociag 2 z ostatnim wagonem pociagu 1
        if (curr.first == curr.first.next) { // gdy byl to jedyny wagon pociagu 1, usuwa pociag
            delTrain(tFirst);

        } else {
            if (curr.first.next.next == curr.first) { // pociag odwrocony
                /*
                 * kolejnosc wagonow dla odwroconego pociagu nie zostala naprawiona przez display
                 * zatem biezemy wagon, ktory zgodnie z kolejnoscia odworoconego pociagu powinnien byc
                 * przedostatni i przypisujemy mu odpowiednie referencje
                 */
                curr.first.prev.prev.prev = curr.first.prev.prev.next; // prev przedostatniego to przedprzedostatni
                curr.first.prev.prev.next = curr.first; // next przedostatniego to pierwszy
                curr.first.prev = curr.first.prev.prev; // prev pierwszego to przedostatni

            } else { // pociag nie zostal odwrocony lub nastapilo display, ktore go naprawilo
                curr.first.prev = curr.first.prev.prev; // prev pierwszego to przedostatni
                curr.first.prev.next = curr.first; // next przedostatniego to pierwszy
            }
        }
    }

    // usuwa pociag
    public void delTrain (String t) {
        Train tmp = trains; // wskazuje na pierwszy pociag na liscie
        if (tmp.trainName.equals(t)) { // jezeli pociag pierwszy na liscie
            trains = tmp.next; // usuwamy pierwszy pociag, przenoszac referencje trains na nastepny na liscie
            return;
        }

        // szukamy pociagu, ktory poprzedza ten, ktory chcemy usunac
        while (tmp.next != null && !tmp.next.trainName.equals(t)) {
            tmp = tmp.next;
        } // tmp - element poprzedzajacy tFirst

        // ustawiamy jako nastepny pociag po poprzedzajacym usuwany ten, ktory nastepuje po usuwanym
        tmp.next = tmp.next.next;
    }

    // lokalizuje pociag
    public Train locate (String t) {
        if (trains == null) { // jezeli nie ma zadnego pociagu od razu zwraca null
            return null;
        }

        Train curr = trains; // pamieta pierwszy pociag

        while (!curr.trainName.equals(t)) { // przechodzimy po pociagach dopoki nie znajdziemy tego o dobrej nazwie
            if (curr.next == null) { // gdy dojdziemy do konca listy
                return null; // zwracamy null
            }
            curr = curr.next; // przechodzimy do kolejnego pociagu
        }
        return curr; // zwracamy referencje do szukanego pociagu
    }
}

/* ---- input ----
4
16
New T1 W1
Display T1
InsertLast T1 W2
Display T1
InsertLast T1 W3
Display T1
InsertLast T1 W4
Display T1
InsertLast T1 W5
Display T1
Reverse T1
Display T1
DelLast T1 T2
Display T1
Display T2
Trains
11
New T1 W1
Display T1
InsertFirst T1 W2
Display T1
InsertFirst T1 W3
Display T1
InsertFirst T1 W4
Display T1
InsertFirst T1 W5
Display T1
Reverse T1
10
New T1 W1
Display T1
InsertLast T1 W2
Display T1
InsertLast T1 W3
Display T1
InsertLast T1 W4
Display T1
InsertLast T1 W5
Display T1
9
New T1 W1
New T2 W2
InsertFirst T2 W3
InsertFirst T2 W4
Display T2
Reverse T2
Display T2
Union T1 T2
Display T1
 */

/* ---- output ----
T1: W1
T1: W1 W2
T1: W1 W2 W3
T1: W1 W2 W3 W4
T1: W1 W2 W3 W4 W5
T1: W5 W4 W3 W2 W1
T1: W5 W4 W3 W2
T2: W1
Trains: T2 T1
T1: W1
T1: W2 W1
T1: W3 W2 W1
T1: W4 W3 W2 W1
T1: W5 W4 W3 W2 W1
T1: W1
T1: W1 W2
T1: W1 W2 W3
T1: W1 W2 W3 W4
T1: W1 W2 W3 W4 W5
T2: W4 W3 W2
T2: W2 W3 W4
T1: W1 W2 W3 W4
 */
