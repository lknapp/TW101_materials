import org.junit.Test;
import org.mockito.InOrder;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SomeObjectTest {

    @Test
    public void testMultipleTimes() {
        List mockedList = mock(List.class);
        mockedList.add("once");

        mockedList.add("twice");
        mockedList.add("twice");

        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");


        //following two verifications work exactly the same - times(1) is used by default
        verify(mockedList).add("once");
        verify(mockedList, times(1)).add("once");

        //exact number of invocations verification
        verify(mockedList, times(2)).add("twice");
        verify(mockedList, times(3)).add("three times");

        //verification using never(). never() is an alias to times(0)
        verify(mockedList, never()).add("never happened");

        //verification using atLeast()/atMost()
        verify(mockedList, atLeastOnce()).add("three times");
        verify(mockedList, atLeast(2)).add("three times");
        verify(mockedList, atMost(5)).add("three times");
    }

    @Test(expected = RuntimeException.class)
    public void testThrowingExceptions() {
        List mockedList = mock(List.class);

        //throwing exceptions
        doThrow(new RuntimeException()).when(mockedList).clear();

        //following throws RuntimeException:
        mockedList.clear();

    }


    @Test
    public void testOrderOnOneMock() {
        // A. Single mock whose methods must be invoked in a particular order
        List singleMock = mock(List.class);

        //using a single mock
        singleMock.add("was added first");
        singleMock.add("was added second");

        //create an inOrder verifier for a single mock
        InOrder inOrder = inOrder(singleMock);

        //following will make sure that add is first called with "was added first, then with "was added second"
        inOrder.verify(singleMock).add("was added first");
        inOrder.verify(singleMock).add("was added second");

    }

    @Test
    public void testOrderOnMultipleMocks() {

        // B. Multiple mocks that must be used in a particular order
        List firstMock = mock(List.class);
        List secondMock = mock(List.class);

        //using mocks
        firstMock.add("was called first");
        secondMock.add("was called second");
        firstMock.add("was called 1.5");

        //create inOrder object passing any mocks that need to be verified in order
        InOrder inOrder = inOrder(secondMock, firstMock);

        //following will make sure that firstMock was called before secondMock
        inOrder.verify(firstMock).add("was called first");
        inOrder.verify(firstMock).add("was called 1.5");
        inOrder.verify(secondMock).add("was called second");
    }

    @Test
    public void testMockReturnsMultipleReturnsInOrder() {
        SomeObject mock = mock(SomeObject.class);

        when(mock.someMethod("arg"))
                .thenReturn("foo")
                .thenReturn("bar");

        assertEquals(mock.someMethod("arg"), "foo");
        assertEquals(mock.someMethod("arg"), "bar");
    }






    @Test(expected = RuntimeException.class)
    public void testExceptionsAndRegularReturns() {
        SomeObject mock = mock(SomeObject.class);

        when(mock.someMethod("some arg"))
                .thenThrow(new RuntimeException())
                .thenReturn("foo");


        //First call: throws runtime exception:
        mock.someMethod("some arg");

        //Second call: prints "foo"
        System.out.println(mock.someMethod("some arg"));

        //Any consecutive call: prints "foo" as well (last stubbing wins).
        System.out.println(mock.someMethod("some arg"));
    }



    @Test
    public void testSpyingOnRealObjects() {

        List list = new LinkedList();
        List spy = spy(list);

        //optionally, you can stub out some methods:
        when(spy.size()).thenReturn(100);

        //using the spy calls *real* methods
        spy.add("one");
        spy.add("two");

        //prints "one" - the first element of a list
        System.out.println(spy.get(0));

        //size() method was stubbed - 100 is printed
        System.out.println(spy.size());

        //optionally, you can verify
        verify(spy).add("one");
        verify(spy).add("two");
    }






}