package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ItemServiceTest {

    @Autowired ItemRepository itemRepository;
    @Autowired ItemService itemService;

    @Test
    public void 아이템_등록() throws Exception{
        //given
        Book book = new Book();
        book.addStock(100);
        book.setName("고뇌");

        //when
        itemService.saveItem(book);

        //then
        assertThat(book).isSameAs(itemService.findOneByName(book.getName()));
    }

    @Test
    public void 아이템_수량_변경() throws Exception{
        //given
        Book book = new Book();
        book.addStock(100);
        book.setName("고뇌");
        itemService.saveItem(book);

        //when
        Item item = itemRepository.findOneByName(book.getName()).get(0);
        item.addStock(50);
        itemService.saveItem(item);

        //then
        assertThat(item.getStockQuantity()).isEqualTo(itemService.findOne(item.getId()).getStockQuantity());
    }

    @Test
    public void 아이템_수량_감소_체크() throws Exception{
        //given
        Book book = new Book();
        book.addStock(100);
        book.setName("고뇌");
        itemService.saveItem(book);

        //when
        Item item = itemRepository.findOneByName(book.getName()).get(0);

        //then
        NotEnoughStockException e = assertThrows(NotEnoughStockException.class, () -> item.removeStock(150));
        assertThat(e.getMessage()).isEqualTo("need more stock");
    }

}