package org.talend.daikon.avro;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;

import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.IndexedRecord;
import org.junit.jupiter.api.Test;
import org.talend.daikon.avro.converter.IndexedRecordConverter;

import java.util.UUID;

/**
 * Unit tests for {AvroRegistry}.
 */
@SuppressWarnings("nls")
public class AvroRegistryTest {

    @Test
    public void testBasic() {
        // Some basic cases for the AvroRegistry.
        AvroRegistry registry = new AvroRegistry();
        assertThat(registry.inferSchema(UUID.fromString("12341234-1234-1234-1234-123412341234")), is(SchemaBuilder.builder()
                .stringBuilder().prop(SchemaConstants.JAVA_CLASS_FLAG, UUID.class.getCanonicalName()).endString()));
    }

    @Test
    public void testAdaptorFactoryWrapPrimitivesAsSingleColumnIndexedRecord() {
        // The input datum is a primitive String.
        Object in = "hello";

        // Get an adapter factory for it.
        IndexedRecordConverter<?, ?> irff = new AvroRegistry().createIndexedRecordConverter(in.getClass());
        assertThat(irff, not(nullValue()));

        // The output is an IndexedRecord that contains one String column.
        @SuppressWarnings({ "rawtypes", "unchecked" })
        IndexedRecord out = (IndexedRecord) ((IndexedRecordConverter) irff).convertToAvro(in);

        Schema s = out.getSchema();
        assertThat(s.getType(), is(Schema.Type.RECORD));
        assertThat(s.getFields(), hasSize(1));
        assertThat(s.getFields().get(0).schema().getType(), is(Schema.Type.STRING));

        // And it simply wraps the incoming data in the first column.
        assertThat(out.get(0), sameInstance(in));
    }

    @Test
    public void testAdaptorFactoryUnconvertedIndexRecords() {
        // Just create a fake object that is known to be an index record.
        Object in = new GenericData().newRecord(null, SchemaBuilder.builder().record("testRecord").fields().name("column1").type()
                .stringType().noDefault().endRecord());

        // Get an adapter factory for it.
        IndexedRecordConverter<?, ?> irff = new AvroRegistry().createIndexedRecordConverter(in.getClass());
        assertThat(irff, not(nullValue()));

        // Since it's already an indexed record, the output is the same instance as the input.
        @SuppressWarnings({ "rawtypes", "unchecked" })
        IndexedRecord out = (IndexedRecord) ((IndexedRecordConverter) irff).convertToAvro(in);
        assertThat(out, sameInstance(in));
    }

    @Test
    public void testAdaptorFactoryUnknownDatum() {
        Object datum = new Object() {
            // Create a datum that is guaranteed to be an unknown class.
        };

        // thrown.expect(RuntimeException.class);
        IndexedRecordConverter<?, ?> irff = new AvroRegistry().createIndexedRecordConverter(datum.getClass());
        assertThat(irff, nullValue());
    }

    @Test
    public void testSerializability() {
        // TODO: Adding shared stuff to the AvroRegistry should be serializable so it can be used across different
        // worker threads on different machines.
    }
}
