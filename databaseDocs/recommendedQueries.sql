use inventory_system;

/*query gives you all records in the inventory table*/
select p.upc, p.invName, i.prod_size, c.categoryName, i.use_by
 from inventory_list i inner join product p on p.ProductID= i.ProductID
 inner join category c  on c.catCode=p.Category;


 /*query to sort/order ALL inventory by name*/
 use inventory_system;
select p.upc, p.invName, i.prod_size, c.categoryName, i.use_by
 from inventory_list i inner join product p on p.ProductID= i.ProductID
 inner join category c  on c.catCode=p.Category
 ORDER by p.invName;


 /*gives a sub selection of just category selected where category 
 variable is selected by radio button*/
 select p.upc, p.invName, i.prod_size, c.categoryName, i.use_by
 from inventory_list i inner join product p on p.ProductID= i.ProductID
 inner join category c  on c.catCode=p.Category
 WHERE c.categoryName Like '%categoryVariable%';

 /* query to sort a selected category by name*/
 select p.upc, p.invName, i.prod_size, c.categoryName, i.use_by
 from inventory_list i inner join product p on p.ProductID= i.ProductID
 inner join category c  on c.catCode=p.Category
 WHERE c.categoryName LIKE '%meats%'
 ORDER by p.invName;