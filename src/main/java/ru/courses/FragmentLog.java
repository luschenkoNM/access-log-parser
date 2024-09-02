package ru.courses;

enum FragmentLog {
    IP {
        @Override
        public String getParameters(String str) {
            String[] parts = str.split(" ");
            return parts[0];
        }
    }, PROPERTIES {
        @Override
        public String getParameters(String str) {
            String[] parts = str.split(" ");
            return parts[1] + parts[2];
        }
    }, DATA {
        @Override
        public String getParameters(String str) {
            String[] parts = str.split(" ");
            String fragment = parts[3];
            return fragment.substring(1, fragment.length());
        }
    }, METHOD {
        @Override
        public String getParameters(String str) {
            String[] parts = str.split(" ");
            return parts[5].substring(1, parts[5].length());
        }
    }, PATH {
        @Override
        public String getParameters(String str) {
            String[] parts = str.split(" ");
            return parts[6];
        }
    }, RESPONSE_CODE {
        @Override
        public String getParameters(String str) {
            String[] parts = str.split(" ");
            return parts[8];
        }
    }, RESPONSE_SIZE {
        @Override
        public String getParameters(String str) {
            String[] parts = str.split(" ");
            return parts[9];
        }
    }, REFERER {
        @Override
        public String getParameters(String str) {
            String[] parts = str.split(" ");
            return parts[10];
        }
    }, USER_AGENT {
        @Override
        public String getParameters(String str) {
            String fragment = "-";
            String[] parts = str.split(" ");
            if (parts.length > 12) {
                for (int i = 11; i < parts.length; i++)
                    fragment += parts[i] + " ";
                fragment = fragment.substring(1, fragment.length() - 1);
            }
            return fragment;
        }

    },
    USER_AGENT_BOT {
        @Override
        public String getParameters(String str) {
            String fragment = "-";
            String[] parts = str.split(" ");
            if (parts.length > 13) {
                fragment = parts[13];
                parts = fragment.split("/");
                fragment = parts[0];
            }
            return fragment;
        }

    };

    public abstract String getParameters(String str);

}
