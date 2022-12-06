package hw;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

  public static void main(String[] args) {

    // correct answers
    String[] key = {"A", "B", "D", "C", "C", "C", "A", "D", "B", "C", "D", "B", "A", "C", "B", "A",
        "C", "D", "C", "D", "A", "D", "B", "C", "D"};

    // candidate Grades (max 40 candidates)
    int[] candidateGrades;

    // Reading candidates file to array
    String[] candidates;
    try {
      candidates = Files.readAllLines(Paths.get("candidates.txt")).toArray(new String[0]);
      ;
      // init candidateGrades array with the same length
      candidateGrades = new int[candidates.length];
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Checking whether the candidates count is greater than 40
    if (candidates.length > 40) {
      System.out.println(
          "Error 0: Your candidates.txt file has too many candidates. The maximum amount is 40.");
      System.exit(-1);
    }

    // Reading departments file to array
    String[] departments;
    try {
      departments = Files.readAllLines(Paths.get("departments.txt")).toArray(new String[0]);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Checking whether the departments count is greater than 10
    if (departments.length > 10) {
      System.out.println(
          "Error 2: Your departments.txt file has too many departments. The maximum amount is 10.");
      System.exit(-1);
    }

    // Checking whether the department max quota is greater than 8
    for (int i = 0; i < departments.length; i++) {
      String department = departments[i];
      String[] departmentInfos = department.split(",");
      // getting quota from department
      int departmentQuota = Integer.parseInt(departmentInfos[2]);
      if (departmentQuota > 8) {
        System.out.println("Error 3: One of your departments have more than 8 quotas available.");
        System.exit(-1);
      }
    }

    // Iterating over candidates list and assigning values to variables
    // formatting output and printing data
    System.out.printf("%-10s %-10s %10s %n", "Number", "Name & Surname", "Grade");
    System.out.printf("%-10s %-10s %10s %n", "...", "...", "...");
    for (int i = 0; i < candidates.length; i++) {
      String candidate = candidates[i];
      String[] candidateInfos = candidate.split(",");

      String no = "";
      String fullName = "";
      int grade = 0;

      // Iterating over candidate info after splitting it by comma
      // setting value for no and fullName
      for (int j = 0; j < candidateInfos.length; j++) {
        // defined candidate variables
        if (j == 0) {
          no = candidateInfos[0];
        } else if (j == 1) {
          fullName = candidateInfos[1];
        }
      }

      // Copying the grades part of array to another array
      String[] candidateAnswers = Arrays.copyOfRange(candidateInfos, 6, candidateInfos.length);
      for (int g = 0; g < candidateAnswers.length; g++) {

        String candidateAnswer = candidateAnswers[g].toLowerCase();
        String correctAnswer = key[g].toLowerCase();

        if (candidateAnswer.equals(correctAnswer)) { // correct answer
          grade += 4;
        } else if (candidateAnswer.equals(" ")) { // no answer
          // do nothing
        } else if (!candidateAnswer.equals(correctAnswer)) { // incorrect answer
          grade -= 3;
        }

      }
      // adding grade to candidateGrades array
      candidateGrades[i] = grade;
      System.out.printf("%-10s %-10s %10s %n", no, fullName, grade);
    }
    System.out.printf("%-10s %-10s %10s %n", "...", "...", "...");

    // implementing bubble sort for sorting candidates by grade
    for (int i = 0; i <= candidateGrades.length; i++) {
      for (int j = i + 1; j < candidateGrades.length; j++) {
        String candidate;
        int temp;
        if (candidateGrades[j] < candidateGrades[i]) {
          temp = candidateGrades[i];
          candidate = candidates[i];
          candidateGrades[i] = candidateGrades[j];
          candidates[i] = candidates[j];
          candidateGrades[j] = temp;
          candidates[j] = candidate;
        }
        // if current candidate and next candidate grades is equal then sort by diplomaGrade
        if (candidateGrades[j] == candidateGrades[i]) {
          String[] currentCandidateInfo = candidates[i].split(",");
          String[] nextCandidateInfo = candidates[j].split(",");
          int currentCandidateDiplomaGrade = Integer.parseInt(currentCandidateInfo[2]);
          int nextCandidateDiplomaGrade = Integer.parseInt(nextCandidateInfo[2]);
          if (nextCandidateDiplomaGrade > currentCandidateDiplomaGrade) {
            temp = candidateGrades[i];
            candidate = candidates[i];
            candidateGrades[i] = candidateGrades[j];
            candidates[i] = candidates[j];
            candidateGrades[j] = temp;
            candidates[j] = candidate;
          }
        }
      }
    }

    // just printing array after bubble sort (for debugging purposes)
    System.out.println("Candidates after implementing bubble sort");
    System.out.printf("%-10s %-10s %10s %10s %n", "Number", "Name & Surname", "Grade",
        "Diploma Grade");
    System.out.printf("%-10s %-10s %10s %10s %n", "...", "...", "...", "...");
    for (int i = 0; i < candidates.length; i++) {
      String[] candidateInfo = candidates[i].split(",");
      String no = candidateInfo[0];
      String fullName = candidateInfo[1];
      int grade = candidateGrades[i];
      int diplomaGrade = Integer.parseInt(candidateInfo[2]);
      System.out.printf("%-10s %-10s %10s %10s %n", no, fullName, grade, diplomaGrade);
    }
    System.out.printf("%-10s %-10s %10s %10s %n", "...", "...", "...", "...");

    // iterating over departments
    String[] candidateDeptNo = new String[candidates.length];
    for (int d = 0; d < departments.length; d++) {
      String[] departmentInfo = departments[d].split(",");
      String departmentNo = departmentInfo[0];
      int departmentQuota = Integer.parseInt(departmentInfo[2]);
      int departmentQuotaCount = 0;
      // iterating over candidates
      for (int c = 0; c < candidates.length; c++) {
        String[] candidateInfo = candidates[c].split(",");
        int candidateNo = Integer.parseInt(candidateInfo[0]);
        int candidateGrade = candidateGrades[c];
        // checking whether candidate grade is under 40
        if (candidateGrade < 40) {
          candidateDeptNo[c] = null;
          continue;
        }
        String[] candidateDeptChoices = Arrays.copyOfRange(candidateInfo, 3, 6);
        for (String departmentChoice : candidateDeptChoices) {
          // check whether department choice of candidate is empty or not
          if (departmentChoice.isEmpty()) continue;
          if (departmentChoice.equals(departmentNo)) {
            candidateDeptNo[c] = departmentNo;
            departmentQuotaCount++;
          }
          // check whether assigned candidates count exceeds quota
          if (departmentQuotaCount > departmentQuota) {
            System.out.println("Max quota count is exceeded");
            System.exit(-1);
          }
          break;
        }
      }
    }

    // Printing departments table
      System.out.printf("%-10s %-10s %10s %n", "No", "Department", "Students");
      System.out.printf("%-10s %-10s %10s %n", "...", "...", "...");
      for (int d = 0; d < departments.length; d++) {
        String[] departmentInfo = departments[d].split(",");
        String departmentNo = departmentInfo[0];
        String departmentName = departmentInfo[1];
        String students = "";
        for (int c = 0; c < candidates.length; c++) {
          if (candidateDeptNo[c] != null && candidateDeptNo[c].equals(departmentNo)) {
            String[] candidateInfo = candidates[c].split(",");
            int candidateNo = Integer.parseInt(candidateInfo[0]);
            students = students + "\t " + candidateNo;
            break;
          }
        }

        System.out.printf("%-10s %-10s %10s %n", departmentNo, departmentName, students);
      }
      System.out.printf("%-10s %-10s %10s %n", "...", "...", "...");

  }
}
