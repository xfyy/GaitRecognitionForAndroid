package ru.oxbao.sensorscollect;


public class Saver
{

    private TestExecutor m_ownerTestExecutor;
    private StorageOutput m_storageOutput;
    private StorageOutputAlternative m_storageOutputAlternative;
    private final String SAVER_TAG = "Saver";
    // private

    public Saver(TestExecutor testExecutor, String prefix)
    {
        m_storageOutput = new StorageOutput(testExecutor, prefix);
        m_storageOutputAlternative = new StorageOutputAlternative(testExecutor, prefix);
        m_ownerTestExecutor = testExecutor;
    }

    public void SaveData(TestData testData, boolean alternative)
    {
            m_storageOutput.SaveInFile(testData);
//        m_storageOutputAlternative.SaveInFile(testData, 'X');
//        m_storageOutputAlternative.SaveInFile(testData, 'Y');
//        m_storageOutputAlternative.SaveInFile(testData, 'Z');
//        m_storageOutputAlternative.SaveInFile(testData, 'T');
    }

    public void SetPrefix(String prefix)
    {
        m_storageOutput.SetPrefix(prefix);
        m_storageOutputAlternative.SetPrefix(prefix);
    }

}
