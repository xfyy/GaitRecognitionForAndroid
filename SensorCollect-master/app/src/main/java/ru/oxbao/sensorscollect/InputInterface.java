package ru.oxbao.sensorscollect;


public class InputInterface
{
    private SensorInput m_sensorInput;
    private StorageInput m_storageInput;
    private TestExecutor m_ownerTestExecutor;
    private Collector m_ownerCollector;

    public enum InputTypeEnum
    {
        sensors,
        storage
    }

    public InputInterface(MainActivity activityTestExecutor,
                          TestExecutor testExecutor, Collector collector)
    {
        m_sensorInput = new SensorInput(activityTestExecutor, testExecutor, collector);
        m_storageInput = new StorageInput(activityTestExecutor, testExecutor, collector);
        m_ownerTestExecutor = testExecutor;
        m_ownerCollector = collector;
    }


    public void Start(InputTypeEnum inputTypeEnum)
    {
        if (inputTypeEnum.equals(InputTypeEnum.sensors))
        {
            // Переопределить Тест дата для фиксированного количества измерений если до этого была работа из файла
            m_ownerTestExecutor.SetFixedTestData();
            m_ownerCollector.SetNumberOfMeasurements(m_ownerTestExecutor.Getm_numberOfMeasurements()); // Возврат к фиксированному значению
            m_sensorInput.Start();
        } else if (inputTypeEnum.equals(InputTypeEnum.storage))
        {
            //Так как неизвестно количество данных в файлах
            // необходимо объявить заново ТестДата. Иначе возможна потеря информации
            int count = m_storageInput.GetNumberLines(); //!!!!!!!!!!!!!!!!!!!!!!!!! проверка на ноль
            m_ownerTestExecutor.g_testData = new TestData(count);
            // Кол-во измерений в коллектрое тоже должно измениться
            m_ownerCollector.SetNumberOfMeasurements(count);

            m_storageInput.Start();
        }

    }

    public void Stop()
    {
        m_sensorInput.Stop();
    }

    public String[] GetFilesNames()
    {
        return m_storageInput.GetFilesFromFolder();
    }


}
