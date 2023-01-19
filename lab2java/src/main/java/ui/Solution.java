package ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

//Dio koda preuzet od vježbe koju sam radio prošle godine iz predmeta UUI 0036515289

public class Solution {

    public static String resolve(Set<ClauseNode> popis,String success){
        Deque<ClauseNode> dq = new LinkedList<>();
        //ispis
        StringBuilder sb = new StringBuilder();
        for(ClauseNode s : popis){
            dq.add(s);
            sb.append(s.getIndex() + ". ")
                    .append(s.getValue() + "\n");
        }
        int j = popis.size() + 1;
        sb.append("===============\n");
        while(!dq.isEmpty()){
            ClauseNode prvi = dq.removeLast();
            int test = dq.size();
            if(dq.isEmpty()){
                sb.append("===============\n").append("[CONCLUSION]: " + success + " is unknown");
                return sb.toString();
            }
            while(true){
                boolean nasoNovu = false;
                ClauseNode drugi = dq.removeLast();
                for(String el : prvi.getElements()){
                    Set<String> tem = drugi.getElements();
                    if(tem.contains(negJedan(el))){
                        Set<String> ostatak = usporedi(prvi.getElements(),drugi.getElements());
                        ClauseNode nova = new ClauseNode(String.join(" v ",ostatak),j);
                        nova.addParent(drugi);
                        nova.addParent(prvi);
                        if(ostatak.size() != 0)
                            sb.append(nova);
                        else{
                            sb.append(j + ". NIL (")
                                    .append(drugi.getIndex())
                                    .append(", " + prvi.getIndex() + ") \n")
                                    .append("===============\n")
                                    .append("[CONCLUSION]: " + success + " is true");
                            return sb.toString();
                        }
                        j++;
                        dq.add(nova);
                        nasoNovu = true;
                        break;
                    }
                }
                if(!nasoNovu) {
                    if(test==0){
                        dq.addFirst(prvi);
                        break;
                    }else{
                        dq.addFirst(drugi);
                        test--;
                    }

                }
                else
                    break;
            }
        }
        return sb.toString();
    }

    public static void cookbook(Set<ClauseNode> popis, List<String> naredbe){
        StringBuilder sb = new StringBuilder();
        System.out.println("Constructed with knowledge:\n");
        for(ClauseNode cn : popis){
            System.out.println(cn.getValue());
        }
        Set<ClauseNode> testSet = new TreeSet<>();
        testSet.addAll(popis);
        for(int i = 0 ; i < naredbe.size() ; i++){
            System.out.println("\nUser's command: " + naredbe.get(i) + "\n");
            if(naredbe.get(i).contains("?")){
                String val = naredbe.get(i);
                val = val.substring(0,val.length()-2);
                String[] kraj = negiraj(val);
                int j = testSet.size() + 1;
                for (String s : kraj) {
                    testSet.add(new ClauseNode(s, j));
                    j++;
                }
                System.out.println(resolve(testSet,val) + "\n");
                testSet = remove(testSet,negJedan(val));

            }else if(naredbe.get(i).contains("+")){
                String val = naredbe.get(i);
                val = val.substring(0,val.length()-2);
                testSet = addNew(testSet,val);
                System.out.println("Added " + val);

            }else if(naredbe.get(i).contains("-")){
                String val = naredbe.get(i);
                val = val.substring(0,val.length()-2);
                testSet = remove(testSet,val);
                System.out.println("Removed " + val);
            }else{
                System.out.println("Nepoznata naredba programa! ");
                return;
            }
        }
        return;
    }

    private static Set<ClauseNode> addNew(Set<ClauseNode> testSet, String val) {
        Set<ClauseNode> temp = new TreeSet<>();
        int i = 1;
        for(ClauseNode cn : testSet){
            temp.add(cn);
            i++;
        }
        ClauseNode novi = new ClauseNode(val,i);
        temp.add(novi);
        return temp;
    }

    private static Set<ClauseNode> remove(Set<ClauseNode> sp , String val){
        Set<ClauseNode> temp = new TreeSet<>();
        int i = 1;
        for(ClauseNode cn : sp){
            if(!(cn.getValue().equals(val))) {
                temp.add(cn);
                i = cn.getIndex() + 1;
            }else break;
        }
        for(ClauseNode cn : sp){
            if(cn.getIndex() > i){
                cn.setIndex(i);
                temp.add(cn);
                i++;
            }
        }
        return temp;
    }
    private static boolean checkTaut(String s){
        List<String> temp = Arrays.stream(s.split(" v ")).collect(Collectors.toList());
        for(String checking : temp) if(temp.contains(negJedan(checking))) return true;
        return false;
    }

    private static Set<String> usporedi(Set<String> prvi,Set<String> drugi){
        Set<String> copy1 = new HashSet<>();
        Set<String> copy2 = new HashSet<>();
        Set<String> temp1 = new HashSet<>();
        for(String s : prvi){
            String temp = negJedan(s);
            temp1.add(temp);
            copy1.add(s);
        }
        Set<String> temp2 = new HashSet<>();
        for(String s : drugi){
            String temp = negJedan(s);
            temp2.add(temp);
            copy2.add(s);
        }
        copy2.removeAll(temp1);
        copy1.removeAll(temp2);
        Set<String> end = new HashSet<>(){{addAll(copy1);addAll(copy2);}};
        return end;
    }

    public static String[] negiraj(String s){
        String[] tem = s.split(" v ");
        if(tem.length==1) {
            return new String[]{negJedan(tem[0])};
        }
        else{
            List<String> temp = new ArrayList<>();
            for(int i = 0; i < tem.length ;i++){
                String[] t = negiraj(tem[i]);
                for(String st : t) temp.add(st);
            }
            return temp.toArray(new String[0]);
        }
    }
    public static String negJedan(String s){
        String temp = null;
        if(s.startsWith("~"))
            temp = s.substring(1);
        else
            temp = "~" + s;
        return temp;
    }


    public static void main(String[] args) throws FileNotFoundException {
        Set<ClauseNode> klauzule = new TreeSet<>();
        List<String> naredbe = new ArrayList<>();
        File datKlauz = new File("");
        File datNar = new File("");
        boolean doICook = false;
        String success = "";
        if(args.length == 2  && args[0].equals("resolution")) datKlauz = new File(args[1]);
        else if(args.length == 3 && args[0].equals("cooking")){
            datKlauz = new File(args[1]);
            datNar = new File(args[2]);
            doICook = true;
        }else{
            System.out.println("Neuspješno pokretanje programa: Netočan način upisa argumenata aplikacije");
            return;
        }

        //dobivanje klauzule iz datoteke
        FileInputStream fis = new FileInputStream(datKlauz);
        Scanner sc = new Scanner(fis);
        String line;
        int i = 1;
        while(sc.hasNextLine()) {
            line = sc.nextLine().toLowerCase(Locale.ROOT);
            if (!(line.startsWith("#"))){
                if(sc.hasNextLine()) {
                    if(!checkTaut(line)) {
                        klauzule.add(new ClauseNode(line, i));
                        i++;
                    }
                }else{
                    if(!checkTaut(line)) {
                        success = line;
                        if(doICook){
                            klauzule.add(new ClauseNode(success,i));
                            i++;
                        }
                        else {
                            String[] kraj = negiraj(line);
                            for (String s : kraj) {
                                klauzule.add(new ClauseNode(s.toLowerCase(Locale.ROOT), i));
                                i++;
                            }
                        }

                    }
                }
            }
        }
        sc.close();

        //provjera cooking metode i dobivanje naredbi iz datoteke
        if(doICook){
            fis = new FileInputStream(datNar);
            sc = new Scanner(fis);
            while(sc.hasNextLine()) {
                line = sc.nextLine().toLowerCase(Locale.ROOT);
                if (!(line.startsWith("#"))) naredbe.add(line);
            }
            sc.close();
        }
        String gotov="";
        if(doICook)
            cookbook(klauzule,naredbe);
        else
            gotov = resolve(klauzule,success);

        System.out.println(gotov);
        return;
    }
}