package com.ga.bank.junittest;

import com.ga.bank.model.account.Account;
import com.ga.bank.model.account.AccountStatus;
import com.ga.bank.model.account.ChekingAccount;
import com.ga.bank.model.account.SavingAccount;
import com.ga.bank.model.card.CardType;
import com.ga.bank.model.card.DebitCard;
import com.ga.bank.repository.FileAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class junit {

    @TempDir
    Path tempDir;

    private FileAccountRepository repository;

    @BeforeEach
    void setUp() {
        // Runs in an isolated temporary directory for every test
        repository = new FileAccountRepository(tempDir.toString());
    }

    @Test
    @DisplayName("Should save and reconstruct ChekingAccount with MASTER_PLATINUM card")
    void testSaveAndFindCheckingAccount() {
        DebitCard card = new DebitCard("4532111122223333", CardType.MASTER_PLATINUM);
        ChekingAccount checking = new ChekingAccount("ACC-101", 1500.0, card, AccountStatus.ACTIVE, 2);

        repository.save(checking);

        Optional<Account> foundOpt = repository.findById("ACC-101");
        assertTrue(foundOpt.isPresent());

        Account found = foundOpt.get();
        assertInstanceOf(ChekingAccount.class, found);

        ChekingAccount parsedChecking = (ChekingAccount) found;
        assertEquals("ACC-101", parsedChecking.getAccountid());
        assertEquals(1500.0, parsedChecking.getBalence(), 0.001);
        assertEquals(AccountStatus.ACTIVE, parsedChecking.getStatus());
        assertEquals(2, parsedChecking.getOverdraftcount());

        assertNotNull(parsedChecking.getDebtcard());
        assertEquals("4532111122223333", parsedChecking.getDebtcard().getCardNumber());
        assertEquals(CardType.MASTER_PLATINUM, parsedChecking.getDebtcard().getCardType());
    }

    @Test
    @DisplayName("Should save and reconstruct SavingAccount with NULL card values")
    void testSaveAndFindSavingAccount() {
        SavingAccount saving = new SavingAccount("ACC-202", 5000.0, null, AccountStatus.ACTIVE);

        repository.save(saving);

        Optional<Account> foundOpt = repository.findById("ACC-202");
        assertTrue(foundOpt.isPresent());

        Account found = foundOpt.get();
        assertInstanceOf(SavingAccount.class, found);

        SavingAccount parsedSaving = (SavingAccount) found;
        assertEquals("ACC-202", parsedSaving.getAccountid());
        assertEquals(5000.0, parsedSaving.getBalence(), 0.001);
        assertEquals(AccountStatus.ACTIVE, parsedSaving.getStatus());
        assertNull(parsedSaving.getDebtcard());
    }

    @Test
    @DisplayName("Should default status to ACTIVE when account status is null")
    void testSaveAccountWithNullStatusDefaultsToActive() {
        SavingAccount saving = new SavingAccount("ACC-303", 250.0, null, null);

        repository.save(saving);

        Optional<Account> foundOpt = repository.findById("ACC-303");
        assertTrue(foundOpt.isPresent());
        assertEquals(AccountStatus.ACTIVE, foundOpt.get().getStatus());
    }

    @Test
    @DisplayName("Should overwrite existing account file on update")
    void testUpdateAccount() {
        ChekingAccount checking = new ChekingAccount("ACC-101", 1000.0, null, AccountStatus.ACTIVE, 0);
        repository.save(checking);


        ChekingAccount updated = new ChekingAccount("ACC-101", 750.0, null, AccountStatus.LOCKED, 1);
        repository.update(updated);

        Optional<Account> foundOpt = repository.findById("ACC-101");
        assertTrue(foundOpt.isPresent());

        ChekingAccount result = (ChekingAccount) foundOpt.get();
        assertEquals(750.0, result.getBalence(), 0.001);
        assertEquals(AccountStatus.LOCKED, result.getStatus());
        assertEquals(1, result.getOverdraftcount());
    }

    @Test
    @DisplayName("Should find account by DebitCard number via lambda stream")
    void testFindByCardNumber() {
        DebitCard card1 = new DebitCard("1111222233334444", CardType.MASTER_STANDARD);
        DebitCard card2 = new DebitCard("9999888877776666", CardType.MASTER_TITANIUM);

        repository.save(new ChekingAccount("ACC-1", 300.0, card1, AccountStatus.ACTIVE, 0));
        repository.save(new SavingAccount("ACC-2", 900.0, card2, AccountStatus.ACTIVE));

        Optional<Account> found = repository.findByCardNumber("9999888877776666");
        assertTrue(found.isPresent());
        assertEquals("ACC-2", found.get().getAccountid());

        Optional<Account> notFound = repository.findByCardNumber("0000000000000000");
        assertTrue(notFound.isEmpty());
    }

    @Test
    @DisplayName("Should return all accounts parsed from txt files")
    void testFindAll() {
        repository.save(new SavingAccount("ACC-A", 100.0, null, AccountStatus.ACTIVE));
        repository.save(new ChekingAccount("ACC-B", 200.0, null, AccountStatus.ACTIVE, 0));

        List<Account> allAccounts = repository.findAll();
        assertEquals(2, allAccounts.size());
    }

    @Test
    @DisplayName("Should return Optional.empty() when account ID does not exist or is null")
    void testFindByIdNotFoundAndNull() {
        assertTrue(repository.findById("NON_EXISTENT").isEmpty());
        assertTrue(repository.findById(null).isEmpty());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when saving or updating null accounts")
    void testSaveAndUpdateNullGuards() {
        assertThrows(IllegalArgumentException.class, () -> repository.save(null));
        assertThrows(IllegalArgumentException.class, () -> repository.update(null));

        ChekingAccount missingIdAccount = new ChekingAccount(null, 100.0, null, AccountStatus.ACTIVE, 0);
        assertThrows(IllegalArgumentException.class, () -> repository.save(missingIdAccount));
        assertThrows(IllegalArgumentException.class, () -> repository.update(missingIdAccount));
    }

    @Test
    @DisplayName("Should safely return Optional.empty() if file data is malformed")
    void testMalformedFileHandling() throws IOException {
        Path malformedFile = tempDir.resolve("ACC-BAD.txt");
        Files.writeString(malformedFile, "ACC-BAD|100.0"); // Less than 4 tokens

        Optional<Account> account = repository.findById("ACC-BAD");
        assertTrue(account.isEmpty());
    }
}