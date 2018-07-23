package com.ycc.club.serialize;

import nu.xom.*;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 2018\7\23 0023 by yancongcong
 */
public class TestXML {

    class Person {

        private String first, last;

        public Person(String first, String last) {
            this.first = first;
            this.last = last;
        }

        public Person(Element person) {
            this.first = person.getFirstChildElement("first").getValue();
            this.last = person.getFirstChildElement("last").getValue();
        }

        @Override
        public String toString() {
            return "Person{" +
                    "first='" + first + '\'' +
                    ", last='" + last + '\'' +
                    '}';
        }

        public Element getXML() {
            Element person = new Element("person");
            Element firstName = new Element("first");
            firstName.appendChild(first);
            Element lastName = new Element("last");
            lastName.appendChild(last);
            person.appendChild(firstName);
            person.appendChild(lastName);
            return person;
        }
    }

    class People extends ArrayList<Person> {

        public People(String filename) throws Exception {
            Document document = new Builder().build(filename);
            Elements childElements = document.getRootElement().getChildElements();
            for (int i = 0; i < childElements.size(); i++) {
                add(new Person(childElements.get(i)));
            }
        }
    }

    public static void format(OutputStream out, Document doc) throws Exception {
        Serializer serializer = new Serializer(out, "UTF-8");
        serializer.setIndent(4);
        serializer.setMaxLength(60);
        serializer.write(doc);
        serializer.flush();
    }

    public static void main(String[] args) throws Exception {
        List<Person> people = Arrays.asList(
                new TestXML().new Person("zhang", "san"),
                new TestXML().new Person("li", "si"),
                new TestXML().new Person("wang", "wu")
        );
        System.out.println(people);

        Element root = new Element("people");
        for (Person p : people)
            root.appendChild(p.getXML());
        Document doc = new Document(root);
        format(System.out, doc);
        format(new BufferedOutputStream(new FileOutputStream("people.xml")), doc);

        People people1 = new TestXML().new People("people.xml");
        System.out.println(people1);
    }

}
