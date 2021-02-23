package com.martynenkoigor.javacore;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

public class JavaIOSkillRepositoryImpl implements SkillRepository {

    private final Map<Long, Skill> skillMap = new TreeMap<>();
    private long nextId = 0L;
    final static private String filePath = "/Users/igor/projects/CRUD/skill.txt";

    JavaIOSkillRepositoryImpl () {
        init();
    }


    @Override
    public Skill create(String skillName) {
        Skill skill = new Skill(nextId, skillName);
        skillMap.put(nextId, skill);
        nextId += 1;
        persist();
        return skill;
    }

    @Override
    public Skill read(Long skillId) {
         Skill skill = skillMap.get(skillId);
         persist();
         return skill;
    }

    @Override
    public Skill update(Skill skill) {
        skillMap.put(skill.getId(),skill);
        persist();
        return skill;
    }

    @Override
    public Skill delete(Long skillId) {
        Skill skill = skillMap.remove(skillId);
        persist();
        return skill;
    }

    private void persist(){
        File file = new File(filePath);
        BufferedWriter bf = null;

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            bf = new BufferedWriter( new FileWriter(file));
            for(Map.Entry<Long, Skill> entry : skillMap.entrySet()) {
                bf.write(entry.getKey() + ";" + entry.getValue().getName());
                bf.newLine();
            }
            bf.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bf.close();
            } catch (Exception e){}
        }
    }

    private Map<Long, Skill> init() {
        BufferedReader br = null;

        try {
            File file = new File(filePath);
            br = new BufferedReader(new FileReader(file));

            String line = null;

            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");

                Long id = Long.parseLong(parts[0].trim());
                String name = parts[1].trim();
                Skill skill = new Skill(id, name);

                if (!id.equals("") && !name.equals("")) {
                    skillMap.put(id, skill);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {}
            }
        }
        return skillMap;

    }
}
