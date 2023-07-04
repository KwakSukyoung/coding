package spell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SpellCorrector implements ISpellCorrector{
    private Trie trie;

    public SpellCorrector() {
        trie = new Trie();
    }

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File myObj = new File(dictionaryFileName);
        Scanner myReader = new Scanner(myObj);
        while(myReader.hasNext()){
            trie.add(myReader.next());
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        inputWord = inputWord.toLowerCase();

        if(trie.find(inputWord)!=null){
            return inputWord;
        }

        ArrayList<String> org = new ArrayList<>();
        ArrayList<String> e1 = new ArrayList<>();
        org.addAll(del(inputWord));
        org.addAll(tra(inputWord));
        org.addAll(alt(inputWord));
        org.addAll(ins(inputWord));

        for(int i =0;i < org.size();++i){
            if(trie.find(org.get(i))!=null){
                e1.add(org.get(i));
            }
        }

        if(e1.size()==1){
            return e1.get(0);
        }

        if(e1.size()>1){
            ArrayList<String> e1f = new ArrayList<>();
            int maxi = -1;
            for(int i =0;i < e1.size();++i){
                int freq = trie.find(e1.get(i)).getValue();
                if(freq >= maxi){
                    if(freq > maxi){
                        e1f.clear();
                        maxi = freq;
                    }
                    e1f.add(e1.get(i));
                }
            }
            Collections.sort(e1f);
            return e1f.get(0);
        }

        else{
            ArrayList<String> org2 = new ArrayList<>();
            ArrayList<String> e2 = new ArrayList<>();
            for(int i=0;i < org.size();++i){
                org2.addAll(del(org.get(i)));
                org2.addAll(tra(org.get(i)));
                org2.addAll(alt(org.get(i)));
                org2.addAll(ins(org.get(i)));

                for(int j= 0; j<org2.size();++j){
                    if(trie.find(org2.get(j))!=null){
                        e2.add(org2.get(j));
                    }
                }
            }

            if(e2.size()==1){
                return e2.get(0);
            }

            if(e2.size()==0){
                return null;
            }

            else{
                ArrayList<String> e2f = new ArrayList<>();
                int maxi = -1;
                for(int i =0;i < e2.size();++i){
                    int freq = trie.find(e2.get(i)).getValue();
                    if(freq >= maxi){
                        if(freq > maxi){
                            e2f.clear();
                            maxi = freq;
                        }
                        e2f.add(e2.get(i));
                    }
                }
                Collections.sort(e2f);
                return e2f.get(0);
            }
        }
    }

    private ArrayList<String> del(String input){
        ArrayList<String> newArray = new ArrayList<>();
        for(int i=0;i < input.length();++i){
            String str = input.substring(0,i) + input.substring(i+1);
            newArray.add(str);
        }
        return newArray;
    }

    private ArrayList<String> tra(String input){
        ArrayList<String> newArray = new ArrayList<>();
        for(int i=0;i < input.length()-1;++i){
            char[] letters = input.toCharArray();
            char myChar = letters[i];
            letters[i] = letters[i+1];
            letters[i+1] = myChar;
            String str = new String(letters);
            newArray.add(str);
        }
        return newArray;
    }

    private ArrayList<String> alt(String input){
        ArrayList<String> newArray = new ArrayList<>();
        for(int i=0;i < input.length();++i){
            char[] letters = input.toCharArray();
            for(int j= 97; j< 123; ++j){
                char myChar = (char) j;
                if(myChar != letters[i]){
                    letters[i] = myChar;
                    String str = new String(letters);
                    newArray.add(str);
                }
            }
        }
        return newArray;
    }

    private ArrayList<String> ins(String input){
        ArrayList<String> newArray = new ArrayList<>();
        for(int i=97;i < 123;++i){
            char myChar = (char) i;
            for(int j= 0; j< input.length()+1; ++j){
                String str = input.substring(0,j)+ myChar+input.substring(j);
                newArray.add(str);
            }
        }
        return newArray;
    }
}
