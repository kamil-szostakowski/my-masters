/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package basejobs;

import jobs.couchdb.CouchdbTestJob;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import runs.IDatabaseRun;

/**
 * Metoda dostarcza podstwowych mechanizmów używanych podczas 
 * wykonywania kazdego zadania na dowolnej bazie danych.
 * 
 * Klasa dostarcza metod do inicjalizacji testu, obejmującej 
 * inicjalizację generatora liczb losowych wykorzystywanego do losowania
 * puli dokumentów której dotyczyły będą ewentualne pobrania danych z bazy.
 * 
 * Inicjalizacji systemu logowania przebiegu testów do plików, z rozbiciem
 * na aktywne wątki oraz strategie testowe.
 * 
 * @author Kamil Szostakowski
 */
public abstract class BaseJob implements Runnable, IDatabaseJob
{
    protected int threadID;
    private String logFilename;
    protected String jobIdentifier;
    protected IDatabaseRun test;
    
    private FileWriter fstream;
    private BufferedWriter output;  
    
    private long testStart;    
    
    protected Random randomizer;  
    
    /*
     * Metoda inicjuje podsystemy wykorzystywane przez wszystkie zadania
     * wykonywane na bazie danych. 
     * 
     * Inicjuje generator liczb losowych odpowiedzialny za generowanie puli
     * dokumentów która będzie pobierana z bazy danych w przypadku dokonywania
     * selectów przez zadanie.
     * 
     * Inicjuje uchwyt do pliku z logami do którego właściwe zadanie będzie logowało
     * szczegółowe informacje o swoim przebiegu.
     */
    
    public void InitTest()
    {
        try 
        {
            this.fstream = new FileWriter(this.logFilename);            
            this.output = new BufferedWriter(fstream);   
            this.randomizer = new Random(System.nanoTime());                        
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(BaseJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
     * Metoda odpowiedzialna za posprzątanie po wyknaniu zadania na bazie danych.
     * 
     * Odpowiada za zamkniecie pliku z logami oraz zlikwidowanie połączenia
     * z bazą danych.
     */
    
    public void CleanupTest()
    {
        long testFinish = System.currentTimeMillis();
        long testExecutionTime = testFinish-this.testStart;   
        
        LogEntry entry = new LogEntry();
        
        entry.SetOperationType("TestFinish");
        entry.SetThreadId(this.threadID);
        entry.SetOperationTime(testExecutionTime);
        
        this.WriteLog(entry);
        
        try 
        {
            this.output.close();
            this.Disconnect();
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(BaseJob.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
     * Metoda dostarczająca zadaniom wykonywanym na bazie danych mechanizmu logowania
     * informacji o swoim przebiego do odpowiednich plików podzielonych względem
     * typu zadania wykonywanego na bazie danych oraz wątku który to zadanie wykonuje.
     */
    
    protected void WriteLog(ILogEntry entry)
    {
        if(this.output == null) { return; }
        
        try 
        {                     
            this.output.write(entry.toString());
            this.output.newLine();
            this.output.flush();
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(BaseJob.class.getName()).log(Level.SEVERE, null, ex);                        
        }
    }
    
    /*
     * Metoda zwracająca listę dokumentów do pobrania z bazy danych dla iteracji
     * przekazanej jako parametr wywołania metody.
     * 
     * Metoda losuje tyle identyfikatorów dokumentu z przeciału 0-1000 ile jest
     * zdefiniowanych w przebiegu przypisanym do danego zadania.
     * 
     * Obecjie metoda nie zwraca uwagi na parametr iter. W przypadku bardziej 
     * finezyjnych testów może to zostać zmienione.
     */
    
    protected int[] GetDocumentListForIter(int iter)
    {
        int[] docs = new int[this.test.GetSelectDocumentCount()];
            
        for(int document=0; document<docs.length; document++)
        {
            docs[document] = (int) Math.abs(this.randomizer.nextInt(1000));                    
        }   
        
        return docs;
    }
    
    /*
     * Metoda definiujaca przebieg dla tego zadania.
     */
    
    @Override
    public void SetConfiguration(IDatabaseRun config)
    {
        this.test = config;
    }
    
    /*
     * Metoda pozwalająca na zdefiniowanie identyfikatora, który będzie
     * przyporządkowany do zadania.
     */
    
    @Override
    public void SetJobIdentifier(String identifier)
    {
        this.jobIdentifier = identifier;
    }
    
    /*
     * Metoda ustala nazwe pliku do którego ma logować wątek
     * wykonujący to zadanie.
     */
    
    @Override
    public void SetLogFile(String filename)
    {
        this.logFilename = filename;          
        
        String[] dirs = filename.split("/");        
        
        String path = String.format("Logs/%s/%s", dirs[1], this.jobIdentifier);
        
        File jobLogDir = new File(path);
        
        if(!jobLogDir.exists())
        {
            jobLogDir.mkdir();
        }        
    }
    
    /*
     * Metoda ustala identyfikator dla watku który będzie wykonywał 
     * to zadanie.
     */
    
    @Override
    public void SetThreadID(int identifier)
    {
        this.threadID = identifier;
    }  
    
    /*
     * Metoda uruchamiajaca wykonanie zadania.
     */
    
    @Override
    public void RunTest() throws FileNotFoundException, IOException
    {
        System.out.println(String.format("Starting thread name: %s id: %d", this.getClass().toString(), this.threadID));                 
        
        this.InitTest();
        
        if(this.Connect())
        {        
            this.testStart = System.currentTimeMillis();        
        
            LogEntry entry = new LogEntry();
        
            entry.SetOperationType("TestStarted");
            entry.SetOperationTime(0);
            entry.SetThreadId(this.threadID);        
        
            this.WriteLog(entry);
        
            for(int iter=0; iter<test.GetRepeatCount(); iter++)
            {            
                long start = System.currentTimeMillis();
                String operationType;
                String extra;
            
                int rand = Math.abs(this.randomizer.nextInt(100));
            
                if(rand < this.test.GetInsertRate())
                {
                    this.PerformInsertOperation(iter);
                    operationType = "insert";
                    extra = "";
                }
            
                else
                {
                    int selected = this.PerformSelectOperation(iter);
                    operationType = "select";
                    extra = String.format("%d", selected);
                }                        
            
                long finish = System.currentTimeMillis();
                long executionTime = finish-start;
            
                LogEntry opEntry = new LogEntry();
            
                opEntry.SetOperationType(operationType);
                opEntry.SetOperationTime(executionTime);
                opEntry.SetThreadId(this.threadID);
                opEntry.SetParameter("iteration", String.format("%d", iter));
            
                if(!extra.equals(""))
                {
                    entry.SetParameter("selected", extra);
                }
            
                this.WriteLog(opEntry);
            }                
        
            System.out.println(String.format("Thread name: %s id: %d finished", this.getClass().toString(), this.threadID));
        
            this.CleanupTest();
        }
    }
    
    /*
     * Abstrakcyjna metoda umożliwiająca zadaniu dziedziczącemu z tej klasy 
     * zdefiniowanie własnego sposobu wykonania operacji select.
     */
    
    @Override
    public int PerformSelectOperation(int identifier)
    {        
        return 0;
    }
    
    /*
     * Abstrakcyjna metoda umożliwiająca zadaniu dziedziczącemu z tej klasy 
     * zdefiniowanie własnego sposobu wykonania operacji insert.
     */    
    
    @Override
    public void PerformInsertOperation(int identifier)
    {        
    }
    
    /*
     * Metoda zdefiniowana przez interfejs Runnable umożliwiająca uruchomienie
     * tego zadania w osobnym wątku.
     */
    
    @Override
    public void run()
    {
        try 
        {
            this.RunTest();
        } 
        
        catch (FileNotFoundException ex) 
        {
            Logger.getLogger(CouchdbTestJob.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        catch (IOException ex) 
        {
            Logger.getLogger(CouchdbTestJob.class.getName()).log(Level.SEVERE, null, ex);
        }                
    } 
    
    
}