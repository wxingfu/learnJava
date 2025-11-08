package com.springframework.test.ioc;

import com.springframework.core.convert.converter.Converter;
import com.springframework.core.convert.support.GenericConversionService;
import com.springframework.core.convert.support.StringToNumberConverterFactory;
import com.springframework.test.common.StringToBooleanConverter;
import com.springframework.test.common.StringToIntegerConverter;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 *
 * @since 2022-06-24
 */
public class TypeConversionFirstPartTest {

    @Test
    public void testStringToIntegerConverter() {
        StringToIntegerConverter converter = new StringToIntegerConverter();
        Integer num = converter.convert("8888");
        Assert.assertEquals(num, (Integer) 8888);
    }

    @Test
    public void testStringToNumberConverterFactory() {
        StringToNumberConverterFactory converterFactory = new StringToNumberConverterFactory();

        Converter<String, Integer> stringToIntegerConverter = converterFactory.getConverter(Integer.class);
        Integer intNum = stringToIntegerConverter.convert("8888");
        Assert.assertEquals(intNum, (Integer) 8888);

        Converter<String, Long> stringToLongConverter = converterFactory.getConverter(Long.class);
        Long longNum = stringToLongConverter.convert("8888");
        Assert.assertEquals(longNum, (Long) 8888L);
    }

    @Test
    public void testGenericConverter() {
        StringToBooleanConverter converter = new StringToBooleanConverter();
        Boolean flag = (Boolean) converter.convert("true", String.class, Boolean.class);
        Assert.assertTrue(flag);
    }

    @Test
    public void testGenericConversionService() {
        GenericConversionService conversionService = new GenericConversionService();
        conversionService.addConverter(new StringToIntegerConverter());

        Integer intNum = conversionService.convert("8888", Integer.class);
        Assert.assertTrue(conversionService.canConvert(String.class, Integer.class));
        Assert.assertEquals(intNum, (Integer) 8888);

        conversionService.addConverterFactory(new StringToNumberConverterFactory());
        Assert.assertTrue(conversionService.canConvert(String.class, Long.class));
        Long longNum = conversionService.convert("8888", Long.class);
        Assert.assertEquals(longNum, (Long) 8888L);


        conversionService.addConverter(new StringToBooleanConverter());
        Assert.assertTrue(conversionService.canConvert(String.class, Boolean.class));
        Boolean flag = conversionService.convert("true", Boolean.class);
        Assert.assertTrue(flag);
    }
}
