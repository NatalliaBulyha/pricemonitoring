package by.senla.training.bulyha.pricemonitoring.service.util;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Component
public class CSVHelper {
    public static String TYPE = "text/csv";
    public static final Logger LOG = Logger.getLogger(CSVHelper.class.getName());

    public boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public <T> List<T> loadObjectList(Class<T> type, String fileName) {
        try {
            CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
            CsvMapper mapper = new CsvMapper();
            mapper.findAndRegisterModules();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            File file = new ClassPathResource(fileName).getFile();
            MappingIterator<T> readValues =
                    mapper.readerFor(type).with(bootstrapSchema).readValues(file);
            return readValues.readAll();
        } catch (Exception e) {
            LOG.warning(String.format("Error occurred while loading object list from file %s", fileName));
            return Collections.emptyList();
        }
    }
}
