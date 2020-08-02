/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import com.kdg.fs24.application.core.log.LogService;
import org.junit.jupiter.api.Test;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.Collection;
import java.nio.CharBuffer;
import java.util.stream.Collectors;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author Козыро Дмитрий
 */
//@ExtendWith(SpringExtension.class)
//  We create a `@SpringBootTest`, starting an actual server on a `RANDOM_PORT`
//@SpringBootTest
public class TestArrays {

    final private Character[] javaCharArray1 = {'q', 'w', 'e', 'r', 't', 'z'};
    final private Character[] javaCharArray2 = {'t', 'y', 'u', 'v', 'i', 'z', 'w'};

    @Test
    public void findUnique() {

        final Collection<Character> list1 = Stream.of(javaCharArray1).distinct().collect(Collectors.toList());
        final Collection<Character> list2 = Stream.of(javaCharArray2).distinct().collect(Collectors.toList());

        final Collection<Character> list3 = list1.stream().distinct().collect(Collectors.toList());

        //Общий список
        list3.addAll(list2);

        // пересекающиеся элементы
        final Collection<String> listUnion = list1
                .stream()
                .filter(el -> list2.contains(el))
                .map(x -> String.valueOf(x))
                .collect(Collectors.toList());

        // уникальные элементы
        final Collection<String> listExcluded = list3
                .stream()
                .filter(el -> !list1.contains(el))
                .map(x -> String.valueOf(x))
                .collect(Collectors.toList());
//        list1.removeAll(list2);
//        
//        list1.forEach(action);
        System.out.println(listUnion
                .stream()
                .reduce(String.format("union elements: ",
                        "ul"),
                        (x, y) -> x.concat(" ").concat(y)));

        System.out.println(listExcluded
                .stream()
                .reduce(String.format("unique elements: ",
                        "ul"),
                        (x, y) -> x.concat(" ").concat(y)));

    }

}
